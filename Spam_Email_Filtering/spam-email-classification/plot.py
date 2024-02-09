import matplotlib.pyplot as plt


def autolabel(bars, values):
    for bar, value in zip(bars, values):
        height = bar.get_height()
        plt.text(
            bar.get_x() + bar.get_width() / 2,
            height,
            f'{value:.4f}',
            ha='center',
            va='bottom',
            fontsize=8,
            color='black',
        )


def print_plot(list_values, list_labels, title, x_label, y_label, color, horizontal_line_value=None):
    plt.figure(figsize=(12, 9))

    # Customize the bar chart appearance
    bars = plt.bar(list_labels, list_values, color=color, edgecolor='black', linewidth=1.2)

    # Add data labels on top of the bars
    mini = min(list_values) - 0.05
    maxi = max(list_values) + 0.015
    plt.ylim(mini, maxi)  # Adjust the y-axis limits for better visualization
    plt.title(title, fontsize=16, fontweight='bold')
    plt.xlabel(x_label, fontsize=14)
    plt.ylabel(y_label, fontsize=14)

    # Customize the horizontal line appearance
    if horizontal_line_value is not None:
        plt.axhline(y=horizontal_line_value, color='red', linestyle='--', linewidth=2,
                    label=f'Horizontal Line at {horizontal_line_value:.2f}')
        plt.legend()

    # Customize the overall layout
    plt.xticks(rotation=30, ha='right', fontsize=12)
    plt.yticks(fontsize=12)
    plt.grid(axis='y', linestyle='--', alpha=0.7)

    autolabel(bars, list_values)

    plt.show()
