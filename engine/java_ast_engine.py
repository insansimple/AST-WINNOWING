import re
import zlib
from typing import List, Set, Dict, Optional, Tuple, Any

# ==============================================================================
# PENGATURAN PARAMETER DEFAULT AST ENGINE (Ubah di sini agar mudah dikoreksi)
# ==============================================================================
DEFAULT_WEIGHT_STRUCTURAL = 0.70            # Bobot kemiripan struktural AST (default: 0.70)
DEFAULT_WEIGHT_SUBTREE = 0.30               # Bobot kemiripan subtree fingerprint AST (default: 0.30)
DEFAULT_THRESHOLD = 0.65                    # Ambang batas deteksi plagiarisme AST (default: 0.65)
# ==============================================================================


def stable_hash(text: str) -> int:
    """Deterministic string hash menggunakan CRC32."""
    return zlib.crc32(text.encode("utf-8"))


class JavaASTNode:
    """Representasi Simpul Pohon Sintaks Abstrak (AST) untuk Kode Java."""
    def __init__(self, node_type: str, value: str = ""):
        self.node_type = node_type
        self.value = value
        self.children: List['JavaASTNode'] = []
        self.modifiers: List[str] = []

    def add_child(self, child: Optional['JavaASTNode']):
        if child is not None:
            self.children.append(child)

    def size(self) -> int:
        count = 1
        for child in self.children:
            count += child.size()
        return count

    def flatten(self) -> List['JavaASTNode']:
        nodes = [self]
        for child in self.children:
            nodes.extend(child.flatten())
        return nodes

    def __repr__(self):
        return f"{self.node_type}:{self.value}"

    def __eq__(self, other):
        if not isinstance(other, JavaASTNode):
            return False
        return self.node_type == other.node_type and self.value == other.value


class JavaLexer:
    """Tokenizer Mandiri untuk Bahasa Pemrograman Java."""
    KEYWORDS = {
        'abstract', 'assert', 'boolean', 'break', 'byte', 'case', 'catch', 'char',
        'class', 'const', 'continue', 'default', 'do', 'double', 'else', 'enum',
        'extends', 'final', 'finally', 'float', 'for', 'goto', 'if', 'implements',
        'import', 'instanceof', 'int', 'interface', 'long', 'native', 'new', 
        'package', 'private', 'protected', 'public', 'return', 'short', 'static',
        'strictfp', 'super', 'switch', 'synchronized', 'this', 'throw', 'throws',
        'transient', 'try', 'void', 'volatile', 'while', 'true', 'false', 'null'
    }

    MODIFIERS = {'public', 'private', 'protected', 'static', 'final', 'abstract', 
                 'synchronized', 'native', 'transient', 'volatile', 'strictfp'}

    def __init__(self, code: str):
        self.code = self._remove_comments(code)
        self.tokens: List[Tuple[str, str, int]] = []
        self._tokenize()

    def _remove_comments(self, code: str) -> str:
        code = re.sub(r'/\*[\s\S]*?\*/', '', code)
        code = re.sub(r'//.*', '', code)
        return code

    def _tokenize(self):
        patterns = [
            ('STRING', r'\"(?:[^\"\\]|\\.)*\"'),
            ('CHAR', r"\'(?:[^\'\\]|\\.)*\'"),
            ('ANNOTATION', r'@\w+'),
            ('FLOAT', r'\d+\.\d*[fFdD]?|\d*\.\d+[fFdD]?'),
            ('INTEGER', r'(?:0[xX][0-9a-fA-F]+|0[bB][01]+|\d+)[lL]?'),
            ('IDENTIFIER', r'[a-zA-Z_]\w*'),
            ('DELIMITER', r'[{}\[\]();,.:]'),
            ('OPERATOR', r'>>>|<<|>>|&&|\|\||[+\-*/%<>=!&|^~?:]=?|[+\-]{2}'),
        ]
        combined_pattern = '|'.join(f'(?P<{name}>{pattern})' for name, pattern in patterns)

        for match in re.finditer(combined_pattern, self.code):
            token_type = match.lastgroup
            token_value = match.group()

            if token_type == 'IDENTIFIER' and token_value in self.KEYWORDS:
                token_type = 'KEYWORD'

            self.tokens.append((token_type, token_value, match.start()))

    def get_tokens(self) -> List[Tuple[str, str, int]]:
        return self.tokens


class StandaloneJavaASTEngine:
    """
    Engine AST Mandiri untuk Java Source Code Plagiarism Detection.
    Mendukung:
    - Recursive-descent parsing komprehensif untuk struktur Java
    - Normalisasi AST (renaming identifier, method, class, literal)
    - Tree Edit Distance (Canonical Sort DFS Structural Similarity)
    - Subtree Fingerprinting Jaccard Similarity
    - Weighted Similarity Score: (weight_structural * Structural + weight_subtree * Subtree Fingerprints)
    """

    # Akses parameter default dari level modul
    DEFAULT_WEIGHT_STRUCTURAL = DEFAULT_WEIGHT_STRUCTURAL
    DEFAULT_WEIGHT_SUBTREE = DEFAULT_WEIGHT_SUBTREE
    DEFAULT_THRESHOLD = DEFAULT_THRESHOLD

    def __init__(
        self,
        weight_structural: float = DEFAULT_WEIGHT_STRUCTURAL,
        weight_subtree: float = DEFAULT_WEIGHT_SUBTREE,
        threshold: float = DEFAULT_THRESHOLD
    ):
        self.tokens: List[Tuple[str, str, int]] = []
        self.pos = 0
        self.weight_structural = weight_structural
        self.weight_subtree = weight_subtree
        self.threshold = threshold

    def _current(self) -> Optional[Tuple[str, str, int]]:
        return self.tokens[self.pos] if self.pos < len(self.tokens) else None

    def _peek(self, offset: int = 1) -> Optional[Tuple[str, str, int]]:
        p = self.pos + offset
        return self.tokens[p] if p < len(self.tokens) else None

    def _consume(self) -> Optional[Tuple[str, str, int]]:
        tok = self._current()
        self.pos += 1
        return tok

    def _check(self, token_type: str, value: str = None) -> bool:
        tok = self._current()
        if not tok:
            return False
        if value:
            return tok[0] == token_type and tok[1] == value
        return tok[0] == token_type

    def _check_keyword(self, val: str) -> bool:
        return self._check('KEYWORD', val)

    def _check_delimiter(self, val: str) -> bool:
        return self._check('DELIMITER', val)

    def _expect(self, token_type: str, value: str = None) -> Optional[Tuple[str, str, int]]:
        if self._check(token_type, value):
            return self._consume()
        return None

    def _expect_colon(self) -> Optional[Tuple[str, str, int]]:
        if self._check('OPERATOR', ':') or self._check('DELIMITER', ':'):
            return self._consume()
        return None

    def parse(self, code: str) -> Optional[JavaASTNode]:
        try:
            lexer = JavaLexer(code)
            self.tokens = lexer.get_tokens()
            self.pos = 0

            root = JavaASTNode("CompilationUnit", "")

            # Package
            if self._check_keyword('package'):
                self._consume()
                while self._current() and not self._check_delimiter(';'):
                    self._consume()
                self._expect('DELIMITER', ';')

            # Imports
            while self._check_keyword('import'):
                self._consume()
                while self._current() and not self._check_delimiter(';'):
                    self._consume()
                self._expect('DELIMITER', ';')

            # Class/Interface declarations
            while self.pos < len(self.tokens):
                type_decl = self._parse_type_declaration()
                if type_decl:
                    root.add_child(type_decl)
                else:
                    self.pos += 1

            return root
        except Exception as e:
            return JavaASTNode("ErrorUnit", str(e))

    def _parse_modifiers(self) -> List[str]:
        mods = []
        while self._current():
            tok = self._current()
            if tok[0] == 'KEYWORD' and tok[1] in JavaLexer.MODIFIERS:
                mods.append(self._consume()[1])
            elif tok[0] == 'ANNOTATION':
                self._consume()
            else:
                break
        return mods

    def _parse_type_declaration(self) -> Optional[JavaASTNode]:
        mods = self._parse_modifiers()
        if self._check_keyword('class'):
            return self._parse_class(mods)
        elif self._check_keyword('interface'):
            return self._parse_interface(mods)
        elif self._check_keyword('enum'):
            return self._parse_enum(mods)
        return None

    def _parse_class(self, modifiers: List[str]) -> Optional[JavaASTNode]:
        if not self._expect('KEYWORD', 'class'):
            return None
        name_token = self._expect('IDENTIFIER')
        if not name_token:
            return None
        node = JavaASTNode("ClassDeclaration", name_token[1])
        node.modifiers = modifiers

        # Extends & implements
        if self._check_keyword('extends'):
            self._consume()
            if self._check('IDENTIFIER'):
                node.add_child(JavaASTNode("Extends", self._consume()[1]))

        if self._check_keyword('implements'):
            self._consume()
            impl_node = JavaASTNode("Implements", "")
            while self._check('IDENTIFIER'):
                impl_node.add_child(JavaASTNode("Interface", self._consume()[1]))
                if not self._expect('DELIMITER', ','):
                    break
            node.add_child(impl_node)

        # Body
        if self._expect('DELIMITER', '{'):
            body = self._parse_class_body()
            for member in body:
                node.add_child(member)
            self._expect('DELIMITER', '}')

        return node

    def _parse_interface(self, modifiers: List[str]) -> Optional[JavaASTNode]:
        if not self._expect('KEYWORD', 'interface'):
            return None
        name_tok = self._expect('IDENTIFIER')
        if not name_tok:
            return None
        node = JavaASTNode("InterfaceDeclaration", name_tok[1])
        node.modifiers = modifiers

        if self._expect('DELIMITER', '{'):
            body = self._parse_class_body()
            for member in body:
                node.add_child(member)
            self._expect('DELIMITER', '}')
        return node

    def _parse_enum(self, modifiers: List[str]) -> Optional[JavaASTNode]:
        if not self._expect('KEYWORD', 'enum'):
            return None
        name_tok = self._expect('IDENTIFIER')
        if not name_tok:
            return None
        node = JavaASTNode("EnumDeclaration", name_tok[1])
        node.modifiers = modifiers

        if self._expect('DELIMITER', '{'):
            while self._check('IDENTIFIER'):
                node.add_child(JavaASTNode("EnumConstant", self._consume()[1]))
                if not self._expect('DELIMITER', ','):
                    break
            self._expect('DELIMITER', ';')
            body = self._parse_class_body()
            for member in body:
                node.add_child(member)
            self._expect('DELIMITER', '}')
        return node

    def _parse_class_body(self) -> List[JavaASTNode]:
        members = []
        while self.pos < len(self.tokens) and not self._check_delimiter('}'):
            mods = self._parse_modifiers()
            if self._check_keyword('class'):
                nested = self._parse_class(mods)
                if nested: members.append(nested)
                continue
            elif self._check_keyword('interface'):
                nested = self._parse_interface(mods)
                if nested: members.append(nested)
                continue

            member = self._parse_member(mods)
            if member:
                members.append(member)
            else:
                self.pos += 1
        return members

    def _parse_member(self, modifiers: List[str]) -> Optional[JavaASTNode]:
        if self._check('IDENTIFIER') or self._check('KEYWORD'):
            first_tok = self._current()
            first_id = first_tok[1]
            saved_pos = self.pos
            self._consume()

            # Generic
            if self._check_delimiter('<'):
                self._skip_generic_params()

            # Array
            while self._check_delimiter('['):
                self._consume()
                self._expect('DELIMITER', ']')

            # Constructor
            if self._check_delimiter('('):
                self.pos = saved_pos
                return self._parse_method(modifiers, None, is_constructor=True)

            # Method or Field
            elif self._check('IDENTIFIER'):
                second_id = self._consume()[1]
                while self._check_delimiter('['):
                    self._consume()
                    self._expect('DELIMITER', ']')

                if self._check_delimiter('('):
                    return self._parse_method_body(modifiers, first_id, second_id)
                elif self._check_delimiter(';') or self._check('OPERATOR', '=') or self._check_delimiter(','):
                    return self._parse_field_rest(modifiers, first_id, second_id)

            self.pos = saved_pos
        return None

    def _parse_method(self, modifiers: List[str], return_type: Optional[str], is_constructor: bool = False) -> Optional[JavaASTNode]:
        name_tok = self._expect('IDENTIFIER')
        if not name_tok:
            return None
        node_type = "Constructor" if is_constructor else "MethodDeclaration"
        node = JavaASTNode(node_type, name_tok[1])
        node.modifiers = modifiers

        if return_type:
            node.add_child(JavaASTNode("ReturnType", return_type))

        if self._expect('DELIMITER', '('):
            params = self._parse_parameters()
            for p in params:
                node.add_child(p)
            self._expect('DELIMITER', ')')

        if self._check_keyword('throws'):
            self._consume()
            while self._check('IDENTIFIER'):
                node.add_child(JavaASTNode("Throws", self._consume()[1]))
                if not self._expect('DELIMITER', ','):
                    break

        if self._check_delimiter('{'):
            body = self._parse_block()
            if body: node.add_child(body)
        else:
            self._expect('DELIMITER', ';')

        return node

    def _parse_method_body(self, modifiers: List[str], return_type: str, method_name: str) -> Optional[JavaASTNode]:
        node = JavaASTNode("MethodDeclaration", method_name)
        node.modifiers = modifiers
        node.add_child(JavaASTNode("ReturnType", return_type))

        if self._expect('DELIMITER', '('):
            params = self._parse_parameters()
            for p in params:
                node.add_child(p)
            self._expect('DELIMITER', ')')

        if self._check_keyword('throws'):
            self._consume()
            while self._check('IDENTIFIER'):
                node.add_child(JavaASTNode("Throws", self._consume()[1]))
                if not self._expect('DELIMITER', ','):
                    break

        if self._check_delimiter('{'):
            body = self._parse_block()
            if body: node.add_child(body)
        else:
            self._expect('DELIMITER', ';')

        return node

    def _parse_field_rest(self, modifiers: List[str], type_name: str, field_name: str) -> Optional[JavaASTNode]:
        node = JavaASTNode("FieldDeclaration", field_name)
        node.modifiers = modifiers
        node.add_child(JavaASTNode("Type", type_name))

        if self._check('OPERATOR', '='):
            self._consume()
            init = self._parse_expression()
            if init: node.add_child(init)

        while self._check_delimiter(','):
            self._consume()
            if self._check('IDENTIFIER'):
                self._consume()
                if self._check('OPERATOR', '='):
                    self._consume()
                    self._parse_expression()

        self._expect('DELIMITER', ';')
        return node

    def _parse_parameters(self) -> List[JavaASTNode]:
        params = []
        while not self._check_delimiter(')') and self.pos < len(self.tokens):
            if self._check('KEYWORD') or self._check('IDENTIFIER'):
                type_name = self._consume()[1]
                if self._check_delimiter('<'):
                    self._skip_generic_params()
                while self._check_delimiter('['):
                    self._consume()
                    self._expect('DELIMITER', ']')
                    type_name += "[]"

                if self._check('IDENTIFIER'):
                    param_name = self._consume()[1]
                    while self._check_delimiter('['):
                        self._consume()
                        self._expect('DELIMITER', ']')
                    p_node = JavaASTNode("Parameter", param_name)
                    p_node.add_child(JavaASTNode("Type", type_name))
                    params.append(p_node)

            if not self._expect('DELIMITER', ','):
                break
        return params

    def _parse_block(self) -> Optional[JavaASTNode]:
        if not self._expect('DELIMITER', '{'):
            return None
        node = JavaASTNode("Block", "")
        while not self._check_delimiter('}') and self.pos < len(self.tokens):
            stmt = self._parse_statement()
            if stmt:
                node.add_child(stmt)
            else:
                self.pos += 1
        self._expect('DELIMITER', '}')
        return node

    def _parse_statement(self) -> Optional[JavaASTNode]:
        if self._check_delimiter('{'):
            return self._parse_block()
        if self._check_keyword('if'):
            return self._parse_if()
        if self._check_keyword('for'):
            return self._parse_for()
        if self._check_keyword('while'):
            return self._parse_while()
        if self._check_keyword('do'):
            return self._parse_do_while()
        if self._check_keyword('switch'):
            return self._parse_switch()
        if self._check_keyword('try'):
            return self._parse_try()
        if self._check_keyword('return'):
            return self._parse_return()
        if self._check_keyword('throw'):
            return self._parse_throw()
        if self._check_keyword('break'):
            self._consume()
            self._expect('DELIMITER', ';')
            return JavaASTNode("Break", "")
        if self._check_keyword('continue'):
            self._consume()
            self._expect('DELIMITER', ';')
            return JavaASTNode("Continue", "")

        return self._parse_expression_statement()

    def _ensure_block(self, stmt: Optional[JavaASTNode]) -> Optional[JavaASTNode]:
        if stmt is None:
            return None
        if stmt.node_type == "Block":
            return stmt
        block = JavaASTNode("Block", "")
        block.add_child(stmt)
        return block

    def _parse_if(self) -> Optional[JavaASTNode]:
        if not self._expect('KEYWORD', 'if'):
            return None
        node = JavaASTNode("If", "")
        if self._expect('DELIMITER', '('):
            cond = self._parse_expression()
            if cond:
                cond_node = JavaASTNode("Condition", "")
                cond_node.add_child(cond)
                node.add_child(cond_node)
            self._expect('DELIMITER', ')')

        then_stmt = self._parse_statement()
        if then_stmt:
            then_node = JavaASTNode("Then", "")
            then_node.add_child(self._ensure_block(then_stmt))
            node.add_child(then_node)

        if self._check_keyword('else'):
            self._consume()
            else_stmt = self._parse_statement()
            if else_stmt:
                else_node = JavaASTNode("Else", "")
                if else_stmt.node_type == "If":
                    else_node.add_child(else_stmt)
                else:
                    else_node.add_child(self._ensure_block(else_stmt))
                node.add_child(else_node)

        return node

    def _parse_for(self) -> Optional[JavaASTNode]:
        if not self._expect('KEYWORD', 'for'):
            return None
        node = JavaASTNode("For", "")
        if self._expect('DELIMITER', '('):
            # Init
            init = self._parse_expression_statement()
            if init:
                init_node = JavaASTNode("Init", "")
                init_node.add_child(init)
                node.add_child(init_node)

            # Cond
            if not self._check_delimiter(';'):
                cond = self._parse_expression()
                if cond:
                    cond_node = JavaASTNode("Condition", "")
                    cond_node.add_child(cond)
                    node.add_child(cond_node)
            self._expect('DELIMITER', ';')

            # Update
            if not self._check_delimiter(')'):
                update = self._parse_expression()
                if update:
                    update_node = JavaASTNode("Update", "")
                    update_node.add_child(update)
                    node.add_child(update_node)
            self._expect('DELIMITER', ')')

        body = self._parse_statement()
        if body: node.add_child(self._ensure_block(body))
        return node

    def _parse_while(self) -> Optional[JavaASTNode]:
        if not self._expect('KEYWORD', 'while'):
            return None
        node = JavaASTNode("While", "")
        if self._expect('DELIMITER', '('):
            cond = self._parse_expression()
            if cond:
                cond_node = JavaASTNode("Condition", "")
                cond_node.add_child(cond)
                node.add_child(cond_node)
            self._expect('DELIMITER', ')')

        body = self._parse_statement()
        if body: node.add_child(self._ensure_block(body))
        return node

    def _parse_do_while(self) -> Optional[JavaASTNode]:
        if not self._expect('KEYWORD', 'do'):
            return None
        node = JavaASTNode("DoWhile", "")
        body = self._parse_statement()
        if body: node.add_child(self._ensure_block(body))
        if self._expect('KEYWORD', 'while'):
            if self._expect('DELIMITER', '('):
                cond = self._parse_expression()
                if cond:
                    cond_node = JavaASTNode("Condition", "")
                    cond_node.add_child(cond)
                    node.add_child(cond_node)
                self._expect('DELIMITER', ')')
        self._expect('DELIMITER', ';')
        return node

    def _parse_switch(self) -> Optional[JavaASTNode]:
        if not self._expect('KEYWORD', 'switch'):
            return None
        node = JavaASTNode("Switch", "")
        if self._expect('DELIMITER', '('):
            expr = self._parse_expression()
            if expr: node.add_child(expr)
            self._expect('DELIMITER', ')')
        if self._expect('DELIMITER', '{'):
            while not self._check_delimiter('}') and self.pos < len(self.tokens):
                if self._check_keyword('case'):
                    self._consume()
                    c_node = JavaASTNode("Case", "")
                    c_expr = self._parse_expression()
                    if c_expr: c_node.add_child(c_expr)
                    self._expect_colon()
                    while not self._check_keyword('case') and not self._check_keyword('default') and not self._check_delimiter('}'):
                        stmt = self._parse_statement()
                        if stmt: c_node.add_child(stmt)
                        else: break
                    node.add_child(c_node)
                elif self._check_keyword('default'):
                    self._consume()
                    self._expect_colon()
                    d_node = JavaASTNode("Default", "")
                    while not self._check_keyword('case') and not self._check_delimiter('}'):
                        stmt = self._parse_statement()
                        if stmt: d_node.add_child(stmt)
                        else: break
                    node.add_child(d_node)
                else:
                    self.pos += 1
            self._expect('DELIMITER', '}')
        return node

    def _parse_try(self) -> Optional[JavaASTNode]:
        if not self._expect('KEYWORD', 'try'):
            return None
        node = JavaASTNode("Try", "")
        try_block = self._parse_block()
        if try_block:
            t_node = JavaASTNode("TryBlock", "")
            t_node.add_child(try_block)
            node.add_child(t_node)

        while self._check_keyword('catch'):
            self._consume()
            catch_node = JavaASTNode("Catch", "")
            if self._expect('DELIMITER', '('):
                if self._check('IDENTIFIER'):
                    catch_node.add_child(JavaASTNode("ExceptionType", self._consume()[1]))
                if self._check('IDENTIFIER'):
                    catch_node.add_child(JavaASTNode("ExceptionVar", self._consume()[1]))
                self._expect('DELIMITER', ')')
            c_block = self._parse_block()
            if c_block: catch_node.add_child(c_block)
            node.add_child(catch_node)

        if self._check_keyword('finally'):
            self._consume()
            f_block = self._parse_block()
            if f_block:
                f_node = JavaASTNode("Finally", "")
                f_node.add_child(f_block)
                node.add_child(f_node)
        return node

    def _parse_return(self) -> Optional[JavaASTNode]:
        if not self._expect('KEYWORD', 'return'):
            return None
        node = JavaASTNode("Return", "")
        if not self._check_delimiter(';'):
            expr = self._parse_expression()
            if expr: node.add_child(expr)
        self._expect('DELIMITER', ';')
        return node

    def _parse_throw(self) -> Optional[JavaASTNode]:
        if not self._expect('KEYWORD', 'throw'):
            return None
        node = JavaASTNode("Throw", "")
        expr = self._parse_expression()
        if expr: node.add_child(expr)
        self._expect('DELIMITER', ';')
        return node

    def _parse_expression_statement(self) -> Optional[JavaASTNode]:
        saved_pos = self.pos
        if self._check('IDENTIFIER') or self._check('KEYWORD'):
            type_name = self._consume()[1]
            while self._check_delimiter('['):
                self._consume()
                if self._check_delimiter(']'):
                    self._consume()
            if self._check('IDENTIFIER'):
                var_name = self._consume()[1]
                if self._check('OPERATOR', '=') or self._check_delimiter(';') or self._check_delimiter(','):
                    node = JavaASTNode("VariableDeclaration", var_name)
                    while self.pos < len(self.tokens) and not self._check_delimiter(';'):
                        if self._check('OPERATOR', '='):
                            self._consume()
                            init = self._parse_expression()
                            if init: node.add_child(init)
                        else:
                            self._consume()
                    self._expect('DELIMITER', ';')
                    return node

        self.pos = saved_pos
        expr = self._parse_expression()
        if expr and self._check_delimiter(';'):
            self._consume()
            return expr

        while self.pos < len(self.tokens) and not self._check_delimiter(';'):
            self.pos += 1
        self._expect('DELIMITER', ';')
        return None

    def _parse_expression(self) -> Optional[JavaASTNode]:
        return self._parse_assignment()

    def _parse_assignment(self) -> Optional[JavaASTNode]:
        left = self._parse_ternary()
        while self._check('OPERATOR', '=') or self._check('OPERATOR', '+=') or self._check('OPERATOR', '-=') or self._check('OPERATOR', '*=') or self._check('OPERATOR', '/='):
            op = self._consume()[1]
            right = self._parse_assignment()
            node = JavaASTNode("Assignment", op)
            if left: node.add_child(left)
            if right: node.add_child(right)
            left = node
        return left

    def _parse_ternary(self) -> Optional[JavaASTNode]:
        node = self._parse_or()
        if self._check('OPERATOR', '?'):
            self._consume()
            ternary = JavaASTNode("Ternary", "")
            ternary.add_child(node)
            t_then = self._parse_expression()
            if t_then: ternary.add_child(t_then)
            self._expect('OPERATOR', ':')
            t_else = self._parse_expression()
            if t_else: ternary.add_child(t_else)
            return ternary
        return node

    def _parse_or(self) -> Optional[JavaASTNode]:
        left = self._parse_and()
        while self._check('OPERATOR', '||'):
            op = self._consume()[1]
            right = self._parse_and()
            node = JavaASTNode("BinaryOp", op)
            if left: node.add_child(left)
            if right: node.add_child(right)
            left = node
        return left

    def _parse_and(self) -> Optional[JavaASTNode]:
        left = self._parse_equality()
        while self._check('OPERATOR', '&&'):
            op = self._consume()[1]
            right = self._parse_equality()
            node = JavaASTNode("BinaryOp", op)
            if left: node.add_child(left)
            if right: node.add_child(right)
            left = node
        return left

    def _parse_equality(self) -> Optional[JavaASTNode]:
        left = self._parse_relational()
        while self._check('OPERATOR', '==') or self._check('OPERATOR', '!='):
            op = self._consume()[1]
            right = self._parse_relational()
            node = JavaASTNode("BinaryOp", op)
            if left: node.add_child(left)
            if right: node.add_child(right)
            left = node
        return left

    def _parse_relational(self) -> Optional[JavaASTNode]:
        left = self._parse_additive()
        while self._check('OPERATOR', '<') or self._check('OPERATOR', '>') or self._check('OPERATOR', '<=') or self._check('OPERATOR', '>='):
            op = self._consume()[1]
            right = self._parse_additive()
            node = JavaASTNode("BinaryOp", op)
            if left: node.add_child(left)
            if right: node.add_child(right)
            left = node
        return left

    def _parse_additive(self) -> Optional[JavaASTNode]:
        left = self._parse_multiplicative()
        while self._check('OPERATOR', '+') or self._check('OPERATOR', '-'):
            op = self._consume()[1]
            right = self._parse_multiplicative()
            node = JavaASTNode("BinaryOp", op)
            if left: node.add_child(left)
            if right: node.add_child(right)
            left = node
        return left

    def _parse_multiplicative(self) -> Optional[JavaASTNode]:
        left = self._parse_unary()
        while self._check('OPERATOR', '*') or self._check('OPERATOR', '/') or self._check('OPERATOR', '%'):
            op = self._consume()[1]
            right = self._parse_unary()
            node = JavaASTNode("BinaryOp", op)
            if left: node.add_child(left)
            if right: node.add_child(right)
            left = node
        return left

    def _parse_unary(self) -> Optional[JavaASTNode]:
        if self._check('OPERATOR', '!') or self._check('OPERATOR', '-') or self._check('OPERATOR', '++') or self._check('OPERATOR', '--'):
            op = self._consume()[1]
            operand = self._parse_unary()
            node = JavaASTNode("UnaryOp", op)
            if operand: node.add_child(operand)
            return node
        return self._parse_postfix()

    def _parse_postfix(self) -> Optional[JavaASTNode]:
        node = self._parse_primary()
        while True:
            if self._check('OPERATOR', '++') or self._check('OPERATOR', '--'):
                op = self._consume()[1]
                postfix = JavaASTNode("PostfixOp", op)
                if node: postfix.add_child(node)
                node = postfix
            elif self._check_delimiter('['):
                self._consume()
                idx = self._parse_expression()
                self._expect('DELIMITER', ']')
                acc = JavaASTNode("ArrayAccess", "")
                if node: acc.add_child(node)
                if idx: acc.add_child(idx)
                node = acc
            elif self._check_delimiter('.'):
                self._consume()
                if self._check('IDENTIFIER'):
                    member_name = self._consume()[1]
                    if self._check_delimiter('('):
                        call = self._parse_method_call(member_name)
                        if call and node: call.children.insert(0, node)
                        node = call
                    else:
                        f_acc = JavaASTNode("FieldAccess", member_name)
                        if node: f_acc.add_child(node)
                        node = f_acc
            elif self._check_delimiter('(') and node and node.node_type == "Identifier":
                call = self._parse_method_call(node.value)
                node = call
            else:
                break
        return node

    def _parse_method_call(self, method_name: str) -> JavaASTNode:
        node = JavaASTNode("MethodCall", method_name)
        if self._expect('DELIMITER', '('):
            while not self._check_delimiter(')') and self.pos < len(self.tokens):
                arg = self._parse_expression()
                if arg: node.add_child(arg)
                if not self._expect('DELIMITER', ','):
                    break
            self._expect('DELIMITER', ')')
        return node

    def _parse_primary(self) -> Optional[JavaASTNode]:
        if self._check_delimiter('('):
            saved = self.pos
            self._consume()
            tok = self._current()
            if tok and (tok[0] == 'KEYWORD' and tok[1] in {'int', 'char', 'long', 'double', 'float', 'boolean', 'byte', 'short', 'void'} or tok[0] == 'IDENTIFIER'):
                type_name = tok[1]
                self._consume()
                while self._check_delimiter('['):
                    self._consume()
                    self._expect('DELIMITER', ']')
                    type_name += '[]'
                if self._check_delimiter(')'):
                    self._consume()
                    target_expr = self._parse_unary()
                    cast_node = JavaASTNode("Cast", type_name)
                    if target_expr:
                        cast_node.add_child(target_expr)
                    return cast_node
            self.pos = saved
            self._consume()
            expr = self._parse_expression()
            self._expect('DELIMITER', ')')
            return expr
        if self._check_keyword('new'):
            return self._parse_new()
        if self._check_keyword('this'):
            self._consume()
            return JavaASTNode("This", "")
        if self._check_keyword('super'):
            self._consume()
            return JavaASTNode("Super", "")
        if self._check('INTEGER'):
            return JavaASTNode("IntLiteral", self._consume()[1])
        if self._check('FLOAT'):
            return JavaASTNode("FloatLiteral", self._consume()[1])
        if self._check('STRING'):
            return JavaASTNode("StringLiteral", self._consume()[1])
        if self._check('CHAR'):
            return JavaASTNode("CharLiteral", self._consume()[1])
        if self._check_keyword('true') or self._check_keyword('false'):
            return JavaASTNode("BoolLiteral", self._consume()[1])
        if self._check_keyword('null'):
            self._consume()
            return JavaASTNode("NullLiteral", "null")
        if self._check('IDENTIFIER'):
            return JavaASTNode("Identifier", self._consume()[1])
        return None

    def _parse_new(self) -> Optional[JavaASTNode]:
        if not self._expect('KEYWORD', 'new'):
            return None
        node = JavaASTNode("New", "")
        if self._check('IDENTIFIER') or self._check('KEYWORD'):
            node.value = self._consume()[1]
            if self._check_delimiter('<'):
                self._skip_generic_params()
            if self._check_delimiter('['):
                node.node_type = "NewArray"
                while self._check_delimiter('['):
                    self._consume()
                    if not self._check_delimiter(']'):
                        sz = self._parse_expression()
                        if sz: node.add_child(sz)
                    self._expect('DELIMITER', ']')
            elif self._check_delimiter('('):
                self._consume()
                while not self._check_delimiter(')') and self.pos < len(self.tokens):
                    arg = self._parse_expression()
                    if arg: node.add_child(arg)
                    if not self._expect('DELIMITER', ','):
                        break
                self._expect('DELIMITER', ')')
        return node

    def _skip_generic_params(self):
        if not self._expect('DELIMITER', '<'):
            return
        depth = 1
        while depth > 0 and self.pos < len(self.tokens):
            if self._check_delimiter('<'): depth += 1
            elif self._check_delimiter('>'): depth -= 1
            self._consume()

    # --- Normalization ---
    def normalize(self, node: Optional[JavaASTNode]) -> Optional[JavaASTNode]:
        if node is None:
            return None

        normalized = JavaASTNode(node.node_type, "")

        if node.node_type in ["ClassDeclaration", "InterfaceDeclaration", "EnumDeclaration"]:
            normalized.value = "CLASS"
        elif node.node_type in ["MethodDeclaration", "Constructor"]:
            normalized.value = "METHOD"
        elif node.node_type in ["FieldDeclaration", "Parameter", "Identifier", "VariableDeclaration", "FieldAccess"]:
            normalized.value = "VAR"
        elif node.node_type in ["Type", "ReturnType", "Cast"]:
            TYPE_WRAPPER_MAP = {
                'Integer': 'int', 'Long': 'long', 'Double': 'double',
                'Float': 'float', 'Boolean': 'boolean', 'Character': 'char',
                'Byte': 'byte', 'Short': 'short', 'Void': 'void'
            }
            clean = node.value.replace('[]', '')
            suffix = '[]' * node.value.count('[]')
            if clean in TYPE_WRAPPER_MAP:
                normalized.value = TYPE_WRAPPER_MAP[clean] + suffix
            else:
                normalized.value = node.value
        elif node.node_type in ["UnaryOp", "PostfixOp"] and node.value in {"++", "--"}:
            normalized.value = node.value
        elif node.node_type == "New":
            normalized.value = "NEW"
        elif node.node_type == "MethodCall":
            normalized.value = "METHOD"
        elif node.node_type in ["IntLiteral", "FloatLiteral"]:
            normalized.value = "NUM"
        elif node.node_type == "StringLiteral":
            normalized.value = "STR"
        else:
            normalized.value = node.value

        for child in node.children:
            norm_c = self.normalize(child)
            if norm_c:
                normalized.add_child(norm_c)

        return normalized

    # --- Structural Comparison & Subtree Fingerprints ---
    def _node_sort_key(self, node: JavaASTNode) -> int:
        h = stable_hash(f"{node.node_type}:{node.value}")
        for child in node.children:
            h = (h * 31 + self._node_sort_key(child)) % (10**9 + 7)
        return h

    def _sorted_flatten(self, node: JavaASTNode) -> List[JavaASTNode]:
        result = [node]
        children = node.children
        if node.node_type in self._ORDER_INSENSITIVE:
            children = sorted(children, key=self._node_sort_key)
        for child in children:
            result.extend(self._sorted_flatten(child))
        return result

    def calculate_tree_edit_distance(self, ast1: JavaASTNode, ast2: JavaASTNode) -> int:
        nodes1 = self._sorted_flatten(ast1)
        nodes2 = self._sorted_flatten(ast2)
        m, n = len(nodes1), len(nodes2)

        dp = [[0] * (n + 1) for _ in range(m + 1)]
        for i in range(m + 1):
            dp[i][0] = i
        for j in range(n + 1):
            dp[0][j] = j

        for i in range(1, m + 1):
            for j in range(1, n + 1):
                cost = 0 if nodes1[i - 1] == nodes2[j - 1] else 1
                dp[i][j] = min(
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1,
                    dp[i - 1][j - 1] + cost
                )
        return dp[m][n]

    def _greedy_string_tiling(self, tokens1: List[str], tokens2: List[str], min_match_len: int = 5) -> int:
        len1 = len(tokens1)
        len2 = len(tokens2)
        if len1 < min_match_len or len2 < min_match_len:
            return 0

        marked1 = [False] * len1
        marked2 = [False] * len2
        total_coverage = 0

        k = min_match_len
        kgram_map = {}
        for i in range(len1 - k + 1):
            gram = tuple(tokens1[i:i+k])
            if gram not in kgram_map:
                kgram_map[gram] = []
            kgram_map[gram].append(i)

        matches = []
        j = 0
        while j <= len2 - k:
            gram = tuple(tokens2[j:j+k])
            if gram in kgram_map:
                for i in kgram_map[gram]:
                    match_len = k
                    while (i + match_len < len1 and j + match_len < len2 and
                           tokens1[i + match_len] == tokens2[j + match_len]):
                        match_len += 1
                    matches.append((match_len, i, j))
            j += 1

        matches.sort(key=lambda x: x[0], reverse=True)

        for match_len, i, j in matches:
            if not any(marked1[i + offset] for offset in range(match_len)) and \
               not any(marked2[j + offset] for offset in range(match_len)):
                for offset in range(match_len):
                    marked1[i + offset] = True
                    marked2[j + offset] = True
                total_coverage += match_len

        return total_coverage

    def calculate_structural_similarity(self, ast1: JavaASTNode, ast2: JavaASTNode) -> float:
        nodes1 = [f"{n.node_type}:{n.value}" for n in ast1.flatten()]
        nodes2 = [f"{n.node_type}:{n.value}" for n in ast2.flatten()]
        m, n = len(nodes1), len(nodes2)
        if m == 0 and n == 0:
            return 1.0
        if m == 0 or n == 0:
            return 0.0

        cov = self._greedy_string_tiling(nodes1, nodes2, min_match_len=5)
        min_sz = min(m, n)
        containment = cov / min_sz if min_sz > 0 else 0.0
        symmetric = (2.0 * cov) / (m + n) if (m + n) > 0 else 0.0
        return 0.50 * containment + 0.50 * symmetric

    def get_subtree_fingerprints(self, node: JavaASTNode, min_size: int = 3) -> Set[int]:
        fingerprints = set()

        def tree_hash(n: JavaASTNode) -> int:
            node_str = f"{n.node_type}:{n.value}"
            h = stable_hash(node_str)
            for child in n.children:
                h = (h * 31 + tree_hash(child)) % (10**9 + 7)
            return h

        def collect(n: JavaASTNode):
            if n.size() >= min_size:
                fingerprints.add(tree_hash(n))
            for child in n.children:
                collect(child)

        if node:
            collect(node)
        return fingerprints

    # --- Pipeline Kanonikalisasi Tingkat Lanjut (Fase A, B, C) ---
    def _is_string_builder_chain(self, node: JavaASTNode) -> bool:
        if node.node_type == 'MethodCall' and node.value == 'toString':
            curr = node.children[0] if node.children else None
            while curr and curr.node_type == 'MethodCall' and curr.value == 'append':
                curr = curr.children[0] if curr.children else None
            if curr and curr.node_type == 'New' and curr.value == 'StringBuilder':
                return True
        return False

    def _extract_string_builder_args(self, node: JavaASTNode) -> List[JavaASTNode]:
        args = []
        curr = node.children[0] if node.children else None
        while curr and curr.node_type == 'MethodCall' and curr.value == 'append':
            if len(curr.children) > 1:
                for a in curr.children[1:]:
                    args.append(a)
            curr = curr.children[0] if curr.children else None
        args.reverse()
        return args

    def _desugar_string_builder(self, node: JavaASTNode) -> JavaASTNode:
        if self._is_string_builder_chain(node):
            args = self._extract_string_builder_args(node)
            if not args:
                return JavaASTNode('StringLiteral', '')
            if len(args) == 1:
                return self._desugar_string_builder(args[0])
            curr_op = JavaASTNode('BinaryOp', '+')
            curr_op.add_child(self._desugar_string_builder(args[0]))
            curr_op.add_child(self._desugar_string_builder(args[1]))
            for next_arg in args[2:]:
                new_op = JavaASTNode('BinaryOp', '+')
                new_op.add_child(curr_op)
                new_op.add_child(self._desugar_string_builder(next_arg))
                curr_op = new_op
            return curr_op

        for i, child in enumerate(node.children):
            node.children[i] = self._desugar_string_builder(child)
        return node

    def _is_line_separator_call(self, node: JavaASTNode) -> bool:
        if node.node_type == 'MethodCall' and node.value == 'print':
            if len(node.children) >= 2:
                arg = node.children[1]
                if arg.node_type == 'MethodCall' and arg.value == 'lineSeparator':
                    return True
        return False

    def _is_system_print_call(self, node: JavaASTNode) -> bool:
        if node.node_type == 'MethodCall' and node.value in {'print', 'println'}:
            if len(node.children) >= 1:
                caller = node.children[0]
                if caller.node_type == 'FieldAccess' and caller.value == 'out':
                    return True
        return False

    def _consolidate_print_statements(self, block_node: JavaASTNode) -> JavaASTNode:
        new_children = []
        i = 0
        while i < len(block_node.children):
            curr = block_node.children[i]
            curr_call = curr.children[0] if curr.node_type == 'ExpressionStatement' and curr.children else curr
            if i + 1 < len(block_node.children):
                next_node = block_node.children[i + 1]
                next_call = next_node.children[0] if next_node.node_type == 'ExpressionStatement' and next_node.children else next_node
                if self._is_system_print_call(curr_call) and curr_call.value == 'print' and self._is_line_separator_call(next_call):
                    curr_call.value = 'println'
                    new_children.append(curr)
                    i += 2
                    continue
            new_children.append(curr)
            i += 1
        block_node.children = new_children
        return block_node

    def _desugar_unboxing(self, node: JavaASTNode) -> JavaASTNode:
        UNBOX_METHODS = {'intValue', 'doubleValue', 'longValue', 'floatValue', 'booleanValue', 'byteValue', 'shortValue'}
        if node.node_type == 'MethodCall' and node.value in UNBOX_METHODS:
            if node.children:
                return self._desugar_unboxing(node.children[0])
        for i, child in enumerate(node.children):
            node.children[i] = self._desugar_unboxing(child)
        return node

    def canonicalize(self, root: Optional[JavaASTNode]) -> Optional[JavaASTNode]:
        if root is None:
            return None
        root = self._desugar_string_builder(root)
        root = self._desugar_unboxing(root)
        for n in root.flatten():
            if n.node_type in {'Block', 'CompilationUnit', 'ClassDeclaration'}:
                self._consolidate_print_statements(n)
        return root

    def calculate_similarity(
        self,
        code1: str,
        code2: str,
        weight_structural: Optional[float] = None,
        weight_subtree: Optional[float] = None
    ) -> float:
        raw1 = self.parse(code1)
        raw2 = self.parse(code2)

        if raw1 is None or raw2 is None:
            return 0.0

        can1 = self.canonicalize(raw1)
        can2 = self.canonicalize(raw2)

        norm1 = self.normalize(can1)
        norm2 = self.normalize(can2)

        if norm1 is None or norm2 is None:
            return 0.0

        structural_sim = self.calculate_structural_similarity(norm1, norm2)
        fp1 = self.get_subtree_fingerprints(norm1)
        fp2 = self.get_subtree_fingerprints(norm2)

        if not fp1 and not fp2:
            fp_sim = 0.0
        else:
            inter = len(fp1.intersection(fp2))
            min_sz = min(len(fp1), len(fp2))
            union = len(fp1.union(fp2))
            containment = inter / min_sz if min_sz > 0 else 0.0
            jaccard = inter / union if union > 0 else 0.0
            fp_sim = 0.50 * containment + 0.50 * jaccard

        ws = weight_structural if weight_structural is not None else self.weight_structural
        wf = weight_subtree if weight_subtree is not None else self.weight_subtree
        return ws * structural_sim + wf * fp_sim

    def detect(
        self,
        code1: str,
        code2: str,
        threshold: Optional[float] = None,
        weight_structural: Optional[float] = None,
        weight_subtree: Optional[float] = None
    ) -> Dict[str, Any]:
        raw1 = self.parse(code1)
        raw2 = self.parse(code2)

        can1 = self.canonicalize(raw1)
        can2 = self.canonicalize(raw2)

        norm1 = self.normalize(can1)
        norm2 = self.normalize(can2)

        structural_sim = self.calculate_structural_similarity(norm1, norm2)
        fp1 = self.get_subtree_fingerprints(norm1)
        fp2 = self.get_subtree_fingerprints(norm2)

        if not fp1 and not fp2:
            fp_sim = 0.0
        else:
            inter = len(fp1.intersection(fp2))
            min_sz = min(len(fp1), len(fp2))
            union = len(fp1.union(fp2))
            containment = inter / min_sz if min_sz > 0 else 0.0
            jaccard = inter / union if union > 0 else 0.0
            fp_sim = 0.50 * containment + 0.50 * jaccard

        th = threshold if threshold is not None else self.threshold
        ws = weight_structural if weight_structural is not None else self.weight_structural
        wf = weight_subtree if weight_subtree is not None else self.weight_subtree
        similarity = ws * structural_sim + wf * fp_sim

        return {
            "similarity": similarity,
            "structural_similarity": structural_sim,
            "fingerprint_similarity": fp_sim,
            "weight_structural": ws,
            "weight_subtree": wf,
            "is_plagiarized": similarity >= th,
            "threshold": th,
            "ast1_size": norm1.size() if norm1 else 0,
            "ast2_size": norm2.size() if norm2 else 0
        }
