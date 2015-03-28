package com.chrisnewland.demofx.util;

import java.util.List;

public class BallGrid
{
	private int[][] grid;
	private int rows;
	private int cols;

	public BallGrid(int cols, int rows)
	{
		this.cols = cols;
		this.rows = rows;
		grid = new int[cols][rows];
	}

	public void setBall(int col, int row)
	{
		setBall(col, row, 1);
	}

	public void setBall(int col, int row, int value)
	{
		grid[col][row] = value;
	}

	public boolean isSet(int col, int row)
	{
		return getBall(col, row) != 0;
	}

	public int getBall(int col, int row)
	{
		return grid[col][row];
	}

	public int getWidth()
	{
		return cols;
	}

	public int getHeight()
	{
		return rows;
	}

	public static BallGrid concatenate(List<BallGrid> grids)
	{
		BallGrid result = null;

		int width = 0;
		int maxHeight = 0;

		for (BallGrid grid : grids)
		{
			width += grid.getWidth();

			if (grid.getHeight() > maxHeight)
			{
				maxHeight = grid.getHeight();
			}
		}

		result = new BallGrid(width, maxHeight);

		int xOffset = 0;

		for (BallGrid grid : grids)
		{
			int yOffset = maxHeight - grid.getHeight();

			for (int col = 0; col < grid.getWidth(); col++)
			{

				for (int row = 0; row < grid.getHeight(); row++)
				{
					result.setBall(xOffset + col, yOffset + row, grid.getBall(col, row));
				}
			}

			xOffset += grid.getWidth();

		}

		return result;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();

		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col < cols; col++)
			{
				if (isSet(col, row))
				{
					builder.append('*');
				}
				else
				{
					builder.append(' ');
				}
			}

			builder.append("\n");
		}

		return builder.toString();
	}
}