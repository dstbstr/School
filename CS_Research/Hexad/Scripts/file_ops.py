from pathlib import Path
from sys import prefix

def next_path(path: Path) -> Path:
	highest = 1 if path.exists() else 0
	prefix = f"{path.stem}_"

	for candidate in path.parent.glob(f"{prefix}*{path.suffix}"):
		suffix = candidate.stem[len(prefix):]
		if candidate.stem.startswith(prefix) and suffix.isdigit():
			highest = max(highest, int(suffix))

		if highest == 0:
			return path

	return path.with_name(f"{path.stem}_{highest+1}{path.suffix}")

def are_files_identical(first: Path, second: Path) -> bool:
	if(first.stat().st_size != second.stat().st_size):
		return False
	with first.open("rb") as f1, second.open("rb") as f2:
		while True:
			b1 = f1.read(4096)
			b2 = f2.read(4096)
			if b1 != b2:
				return False
			if not b1:
				return True

def deduplicate(base_path: Path) -> None:
    prefix = f"{base_path.stem}_"
    numbered = [
        candidate 
        for candidate in base_path.parent.glob(f"{prefix}*{base_path.suffix}")
        if candidate.stem[len(prefix):].isdigit()
    ]
    numbered.sort(key=lambda p: int(p.stem[len(prefix):]))

    candidates = ([base_path] if base_path.exists() else []) + numbered
    kept = []

    for candidate in candidates:
        if any(are_files_identical(candidate, previous) for previous in kept):
            candidate.unlink()
        else:
            kept.append(candidate)