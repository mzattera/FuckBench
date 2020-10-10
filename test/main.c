#include <stdio.h>

int main() {
	char word[1024];

	printf("Hello world.\n");

	while (1) {
		scanf("%s", word);
		printf("%s\n", word);
		printf("-------------\n");
	}
	
	return 0;
}
