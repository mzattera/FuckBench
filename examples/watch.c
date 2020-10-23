#include <stdio.h>
#include "..\cc65\bftime.h"

int main() {
	
	clock_t last = 0;
	clock_t current = last;

	while (1) {
		while (current == last) {
			current = clock() / CLOCKS_PER_SEC;
		}
		printf ("Time passed [s]: %lu\n", current);
		last = current;
	}
	return 0;
}