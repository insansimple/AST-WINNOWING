import argparse
import json
import os
import platform
import subprocess
import sys
from typing import Dict, Any


def get_hardware_windows() -> Dict[str, Any]:
    """
    Mengambil informasi hardware pada Windows menggunakan PowerShell CIM/WMI
    dengan skema yang persis sama dengan hardware.json.
    """
    ps_script = """
    $comp = Get-CimInstance Win32_ComputerSystem | Select-Object TotalPhysicalMemory
    $os = Get-CimInstance Win32_OperatingSystem | Select-Object Caption, Version, OSArchitecture
    $cpu = @(Get-CimInstance Win32_Processor | Select-Object Name, NumberOfCores, NumberOfLogicalProcessors)

    $result = [PSCustomObject]@{
        Computer = $comp
        OS = $os
        CPU = $cpu
        note = "Hardware of the recorded experiment host; update explicitly if reproducing on another host."
    }
    $result | ConvertTo-Json -Depth 4
    """
    res = subprocess.run(
        ["powershell", "-NoProfile", "-NonInteractive", "-Command", ps_script],
        capture_output=True,
        text=True,
        check=True
    )
    data = json.loads(res.stdout)

    # Pastikan CPU selalu berbentuk list
    if isinstance(data.get("CPU"), dict):
        data["CPU"] = [data["CPU"]]
    elif not isinstance(data.get("CPU"), list):
        data["CPU"] = [data.get("CPU")]

    # Pastikan tipe data bilangan bulat sesuai skema
    if "Computer" in data and "TotalPhysicalMemory" in data["Computer"]:
        data["Computer"]["TotalPhysicalMemory"] = int(data["Computer"]["TotalPhysicalMemory"])

    if "CPU" in data and isinstance(data["CPU"], list):
        for cpu in data["CPU"]:
            if "NumberOfCores" in cpu:
                cpu["NumberOfCores"] = int(cpu["NumberOfCores"])
            if "NumberOfLogicalProcessors" in cpu:
                cpu["NumberOfLogicalProcessors"] = int(cpu["NumberOfLogicalProcessors"])

    return data


def get_hardware_fallback() -> Dict[str, Any]:
    """
    Fallback untuk lingkungan non-Windows atau jika PowerShell CIM tidak tersedia.
    """
    total_mem = 0
    # Coba baca memori pada Unix/Linux
    if os.path.exists("/proc/meminfo"):
        try:
            with open("/proc/meminfo", "r") as f:
                for line in f:
                    if line.startswith("MemTotal:"):
                        total_mem = int(line.split()[1]) * 1024
                        break
        except Exception:
            pass
    elif hasattr(os, "sysconf"):
        try:
            total_mem = os.sysconf("SC_PAGE_SIZE") * os.sysconf("SC_PHYS_PAGES")
        except Exception:
            pass

    cores = os.cpu_count() or 1
    cpu_info = [{
        "Name": platform.processor() or "Unknown CPU",
        "NumberOfCores": cores,
        "NumberOfLogicalProcessors": cores
    }]

    os_info = {
        "Caption": f"{platform.system()} {platform.release()}",
        "Version": platform.version(),
        "OSArchitecture": platform.architecture()[0]
    }

    return {
        "Computer": {
            "TotalPhysicalMemory": total_mem
        },
        "OS": os_info,
        "CPU": cpu_info,
        "note": "Hardware of the recorded experiment host; update explicitly if reproducing on another host."
    }


def get_hardware_info() -> Dict[str, Any]:
    """
    Mengambil data hardware mesin saat ini.
    """
    if platform.system() == "Windows":
        try:
            return get_hardware_windows()
        except Exception as e:
            print(f"[WARNING] Gagal mengambil info WMI via PowerShell: {e}. Menggunakan fallback.", file=sys.stderr)
            return get_hardware_fallback()
    else:
        return get_hardware_fallback()


def main():
    default_dir = os.path.dirname(os.path.abspath(__file__))
    default_output = os.path.join(default_dir, "hardware.json")

    parser = argparse.ArgumentParser(
        description="Merekam spesifikasi hardware host eksperimen ke format JSON (hardware.json)."
    )
    parser.add_argument(
        "--output", "-o",
        type=str,
        default=default_output,
        help="Path file JSON target output (default: hardware.json di direktori yang sama)"
    )
    parser.add_argument(
        "--no-save",
        action="store_true",
        help="Hanya tampilkan ke stdout tanpa menyimpan ke file"
    )

    args = parser.parse_args()

    hardware = get_hardware_info()
    formatted_json = json.dumps(hardware, indent=2)

    print(formatted_json)

    if not args.no_save:
        with open(args.output, "w", encoding="utf-8") as f:
            f.write(formatted_json + "\n")
        print(f"\n[OK] Spesifikasi hardware berhasil disimpan ke: {args.output}")


if __name__ == "__main__":
    main()
