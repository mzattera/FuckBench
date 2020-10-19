// Test BRK (interrupts).

#include <stdio.h>

int main() {
	 __asm__ ("BRK") ;
	return 0;
}