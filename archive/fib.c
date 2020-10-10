#include <stdio.h>

int fib (int n) {
	if (n == 0)
		return 0;
	else if (n == 1)
		return 1;
	else 
		return fib(n - 1) + fib(n - 2);
}


int main () {
	int i;
	for (i = 0; i < 5; i++)
		printf("n=\t%d\tfib(n)=%d\n", i, fib(i));

	return 0;
}
