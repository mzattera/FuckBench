#include <stdio.h>
#include "..\cc65\bftime.h"

int main() {
	
	unsigned long last = 0;
	unsigned long current = 0;

	while (1) {
		while (current == last) {
			get_instr_count(&current);
			current /= 6000;
		}
		printf ("Time passed (s): %lu\n", current);
		last = current;
	}
	return 0;
}