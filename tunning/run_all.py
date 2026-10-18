import os
import subprocess
import sys

CURRENT_DIR = os.path.dirname(os.path.abspath(__file__))

TUNING_SCRIPTS = [
    "tunning_winnowing.py",
    "tunning_ast.py",
    "tunning_hybrid.py",
]


def main():
    for script in TUNING_SCRIPTS:
        script_path = os.path.join(CURRENT_DIR, script)
        cmd = [sys.executable, script_path] + sys.argv[1:]
        result = subprocess.run(cmd, cwd=CURRENT_DIR)
        if result.returncode != 0:
            sys.exit(result.returncode)


if __name__ == "__main__":
    main()
