#include <stdio.h>
#include "fbtime.h"

int main() {

	time_t last = 0;
	time_t current = last;

	printf ("CLOCKS_PER_SEC value is: %lu\n", CLOCKS_PER_SEC);
	printf ("CLK_TCK value is:        %lu\n\n", CLK_TCK);

	while (1) {
		while (current == last) {
			time (&current);
		}
		printf ("Time passed [s]: %lu\n", current);
		last = current;
	}
	return 0;
}