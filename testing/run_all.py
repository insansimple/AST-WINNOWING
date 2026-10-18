import os
import subprocess
import sys

CURRENT_DIR = os.path.dirname(os.path.abspath(__file__))
PUBLIKASI_DIR = os.path.dirname(CURRENT_DIR)

TEST_SCRIPTS = [
    os.path.join(CURRENT_DIR, "test_winnowing.py"),
    os.path.join(CURRENT_DIR, "test_ast.py"),
    os.path.join(CURRENT_DIR, "test_hybrid.py"),
    os.path.join(PUBLIKASI_DIR, "jplag", "test_jplag.py"),
]


def main():
    for script in TEST_SCRIPTS:
        script_path = os.path.abspath(script) if os.path.isabs(script) else os.path.abspath(os.path.join(CURRENT_DIR, script))
        script_dir = os.path.dirname(script_path)
        cmd = [sys.executable, script_path] + sys.argv[1:]
        result = subprocess.run(cmd, cwd=script_dir)
        if result.returncode != 0:
            sys.exit(result.returncode)


if __name__ == "__main__":
    main()
