/*
Implements library time functions.
*/

#include <stddef.h>
#include "fbtime.h"

int __fastcall__ clock_getres (clockid_t clock_id, struct timespec *res) {
	if (res != NULL) {
		res->tv_sec = 1;
		res->tv_nsec = 0;
	}
	
	return 0;
}

int __fastcall__ clock_gettime (clockid_t clock_id, struct timespec *tp) {
	if (tp != NULL) {
		tp->tv_sec = clock() / CLOCKS_PER_SEC;
		tp->tv_nsec = 0;
	}
	
	return 0;
}
