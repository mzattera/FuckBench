/*
Implements C library functions for time.
*/

#include <stddef.h>
#include <time.h>

/* This is the value of CLOCKS_PER_SEC */
#define CPS ((clock_t)13409)

clock_t _clocks_per_sec (void) {
	return CPS;
}

int __fastcall__ clock_getres (clockid_t clock_id, struct timespec *res) {
	if (res != NULL) {
		res->tv_sec = 1;
		res->tv_nsec = 0;
	}
	
	return 0;
}

int __fastcall__ clock_gettime (clockid_t clock_id, struct timespec *tp) {
	if (tp != NULL) {
		tp->tv_sec = clock() / CPS;
		tp->tv_nsec = 0;
	}
	
	return 0;
}
