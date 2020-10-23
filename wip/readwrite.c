#include <stdio.h>

int main() {
	char buf[1024];
	
	while (1) {
		scanf("%s", buf);
		printf ("%s<\n", buf);
		printf ("===============================\n");
	}
	return 0;
}