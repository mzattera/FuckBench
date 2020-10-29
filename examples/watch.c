#include <stdio.h>
#include <time.h>

int main() {

	time_t last = 0;
	time_t current = last;

	printf ("CLOCKS_PER_SEC value is: %lu\n", CLOCKS_PER_SEC);

	while (1) {
		while (current == last) {
			time (&current);
		}
		printf ("Time passed [s]: %lu\n", current);
		last = current;
	}
	return 0;
}