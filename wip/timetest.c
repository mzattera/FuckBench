#include <stdio.h>
#include "..\cc65\bftime.h"

int main() {
	
	clock_t clk_time = clock();
	printf ("Time passed (s): %lu\n", clk_time);

	return 0;
}
