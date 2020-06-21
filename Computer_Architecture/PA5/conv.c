#include "conv.h"
void convolution(const int M, const int N, const int *input, int *output, const int filter[3][3])
{
	register int f1 = filter[0][0];
	register int f2 = filter[0][1];
	register int f3 = filter[0][2];
	register int f4 = filter[1][0];
	register int f5 = filter[1][1];
	register int f6 = filter[1][2];
	register int f7 = filter[2][0];
	register int f8 = filter[2][1];
	register int f9 = filter[2][2];
	register int j = 0;
	register int next_row = N;
	register int pre_row_input;
	register int j_input;
	register int next_row_input;
	register int pre_o;
	register int o;
	register int next_o;
	j_input = input[j];
	next_row_input = input[next_row];
	output[j] += j_input * f5 + next_row_input * f8;
	output[j + 1] += j_input * f4 + next_row_input * f7;
	++j;
	++next_row;
	while (j < N - 1)
	{
		j_input = input[j];
		next_row_input = input[next_row];
		output[j - 1] += j_input * f6 + next_row_input * f9;
		output[j] += j_input * f5 + next_row_input * f8;
		output[j + 1] += j_input * f4 + next_row_input * f7;
		++j;
		++next_row;
	}
	j_input = input[j];
	next_row_input = input[next_row];
	output[j - 1] += j_input * f6 + next_row_input * f9;
	output[j] += j_input * f5 + next_row_input * f8;
	++j;
	++next_row;
	register int pre_row = 0;
	int row_last = N - 1;
	const int last = M * N;
	while (next_row < last)
	{
		pre_row_input = input[pre_row];
		j_input = input[j];
		next_row_input = input[next_row];
		o = pre_row_input * f2 + j_input * f5 + next_row_input * f8;
		next_o = pre_row_input * f1 + j_input * f4 + next_row_input * f7;
		++j;
		++pre_row;
		++next_row;
		while (pre_row < row_last)
		{
			pre_row_input = input[pre_row];
			j_input = input[j];
			next_row_input = input[next_row];
			pre_o = o;
			o = next_o;
			pre_o += pre_row_input * f3 + j_input * f6 + next_row_input * f9;
			o += pre_row_input * f2 + j_input * f5 + next_row_input * f8;
			next_o = pre_row_input * f1 + j_input * f4 + next_row_input * f7;
			output[j - 1] = pre_o;
			++j;
			++pre_row;
			++next_row;
		}
		pre_row_input = input[pre_row];
		j_input = input[j];
		next_row_input = input[next_row];
		pre_o = o;
		o = next_o;
		pre_o += pre_row_input * f3 + j_input * f6 + next_row_input * f9;
		o += pre_row_input * f2 + j_input * f5 + next_row_input * f8;
		output[j - 1] = pre_o;
		output[j] = o;
		++j;
		++pre_row;
		++next_row;
		row_last += N;
	}
	pre_row_input = input[pre_row];
	j_input = input[j];
	output[j] += pre_row_input * f2 + j_input * f5;
	output[j + 1] += pre_row_input * f1 + j_input * f4;
	++j;
	++pre_row;
	while (j < last - 1)
	{
		pre_row_input = input[pre_row];
		j_input = input[j];
		output[j - 1] += pre_row_input * f3 + j_input * f6;
		output[j] += pre_row_input * f2 + j_input * f5;
		output[j + 1] += pre_row_input * f1 + j_input * f4;
		++j;
		++pre_row;
	}
	pre_row_input = input[pre_row];
	j_input = input[j];
	output[j - 1] += pre_row_input * f3 + j_input * f6;
	output[j] += pre_row_input * f2 + j_input * f5;
}