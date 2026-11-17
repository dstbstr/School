import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.ticker import MaxNLocator
from pathlib import Path
import file_ops

def get_data() -> pd.DataFrame:
	game_data = [
		["Collection and Trading", .153, .148, .172, 0, .259, 0],
		["Gifting", .163, 0, 0, 0, .259, 0],
		["Knowledge Sharing", .184, .138, 0, .167, .231, 0],
		["Administrative roles", 0, 0, 0, .199, 0, 0],

		["Guilds or Teams", .179, 0, 0, .169, .192, 0],
		["Social Networks", .150, 0, 0, .197, .143, 0],
		["Social Comparison or Pressure", .152, 0, 0, .197, .143, 0],
		["Social Competition", .216, .249, .161, .320, .239, 0],
		["Social Discovery", .205, 0, 0, .179, .217, 0],

		["Exploratory Task", 0, .352, 0, 0, .152, .139],
		["Nonlinear Gameplay", 0, .221, 0, 0, 0, .179],
		["Easter Eggs", .137, .246, 0, .153, .162, 0],
		["Unlockable or Rare Content", 0, .225, 0, 0, .149, .140],
		["Creativity Tools", 0, .230, 0, .252, 0, 0],
		["Customization", 0, .198, 0, .136, .162, 0],

		["Challenges", 0, .412, .463, .207, .317, .212],
		["Certificates", .142, .200, .229, 0, .228, 0],
		["Learning", 0, .391, .215, 0, 0, 0],
		["Quests", 0, .236, .266, 0, .245, 0],
		["Levels or Progression", .170, .204, .239, 0, .302, 0],
		["Boss Battles", 0, 0, 0, 0, 0, 0],

		["Points", .168, .201, .172, 0, .259, 0],
		["Rewards or Prizes", 0, .139, .167, 0, .301, 0],
		["Leaderboards", .199, 0, 0, .170, .276, 0],
		["Badges or Achievements", .164, 0, .208, 0, .271, 0],
		["Virtual Economy", 0, 0, 0, 0, .273, 0],
		["Lotteries or Games of Chance", .148, 0, 0, 0, .190, 0],

		["Innovation Platforms", 0, 0, 0, .302, .166, 0],
		["Voting Mechanisms", 0, 0, 0, .236, .138, 0],
		["Development Tools", 0, 0, 0, .294, .144, 0],
		["Anonymity", 0, .318, .289, 0, .211, 0],
		["Anarchic Gameplay", 0, .285, 0, .268, 0, 0]
	]
	columns = ["Mechanic", "Socializer", "Free Spirit", "Achiever", "Disruptor", "Player", "Philanthropist"]
	result = pd.DataFrame(data = game_data, columns = columns)
	return result


def plot_heatmap(data: pd.DataFrame, figures_dir: Path) -> None:
	values = data.set_index("Mechanic").T
	fig, ax = plt.subplots(figsize=(18, 5), constrained_layout=True)

	image = ax.imshow(
		values.to_numpy(),
		aspect="auto",
		cmap="viridis",
		vmin=0,
		vmax=values.to_numpy().max()
	)

	ax.set_xticks(range(len(values.columns)), labels=values.columns, rotation=60, ha="right")
	ax.set_yticks(range(len(values.index)), labels=values.index)
	ax.set_title("Game Mechanics By Hexad Type")

	for row in range(len(values.index)):
		for col in range(len(values.columns)):
			ax.text(col, row, "", ha="center", va="center", fontsize=7)
			#ax.text(col, row, f"{values.iloc[row, col]:.3f}", ha="center", va="center", fontsize=7)

	fig.colorbar(image, ax=ax, label="Kendall’s τ")
	fig.savefig(file_ops.next_path(figures_dir / "heatmap.png"), format="png", dpi=500, bbox_inches="tight")
	file_ops.deduplicate(figures_dir / "heatmap.png")
	plt.close(fig)
			

def main():
	data = get_data()

	figures_dir = Path(__file__).resolve().parent.parent / "Figures"
	figures_dir.mkdir(parents=True, exist_ok=True)

	plot_heatmap(data, figures_dir)

	print("Done")
if __name__ == "__main__":
	main()

