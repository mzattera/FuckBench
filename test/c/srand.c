#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main () {
   int i, n;
   
   n = 5;
   
   /* Intializes random number generator */
   srand((unsigned) time(NULL));

   /* Print 5 random numbers from 0 to 50 */
   for( i = 0 ; i < n ; i++ ) {
      printf("%d\n", rand() % 50);
   }
   
   return(0);
}