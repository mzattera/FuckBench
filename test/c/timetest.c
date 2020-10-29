#include <stdio.h>
#include <time.h>

void printtm (struct tm* _tm) {
	printf ("[YY=%d MM=%d DOY=%d hh=%d mm=%d ss=%d]\n", _tm->tm_year, _tm->tm_mon, _tm->tm_yday, _tm->tm_hour, _tm->tm_min, _tm->tm_sec);
}

int main() {

	clock_t cpu_time = 0;
	time_t calendar_time = 0;	
	time_t calendar_time2 = 0;	
	struct tm* _tm;
	char buf[128];
	struct timespec ts;

	/* ISO C function prototypes */

	// clock_t clock (void); -> missing in lib
	cpu_time = clock();
	if (cpu_time == (clock_t)(-1)) 
		printf("Error with clock()");
	else 
		printf ("CPU time passed (# instructions): %lu\n", (unsigned long)cpu_time);

	// time_t __fastcall__ time (time_t* t); -> uses clock_gettime()
	calendar_time = time (&calendar_time);
	if (calendar_time == (time_t)(-1)) 
		printf("Error with time()");
	else 
		printf ("Calendar time: %lu\n", (unsigned long)calendar_time);
		
	// char* __fastcall__ ctime (const time_t* timep);
	printf ("ctime(): %s\n", ctime(&calendar_time));

	// struct tm* __fastcall__ gmtime (const time_t* timep);
	_tm = gmtime(&calendar_time);
	printf ("gmtime():\t");
	printtm(_tm);

	// struct tm* __fastcall__ localtime (const time_t* timep);
	_tm = localtime(&calendar_time);
	printf ("localtime():\t");
	printtm(_tm);

	// time_t __fastcall__ mktime (struct tm* timep);
	calendar_time2 = mktime(_tm);
	printf("%lu -> %lu\n", calendar_time, calendar_time2);

	// char* __fastcall__ asctime (const struct tm* timep);
	printf ("asctime(): %s\n", asctime(_tm));

	// size_t __fastcall__ strftime (char* buf, size_t bufsize, const char* format, const struct tm* tm);
	if (strftime (buf, 256, "%c", _tm) == 0)
		printf("Error with strftime()");
	else 
		printf ("strftime(): %s\n", buf);
	
	
	
	/* POSIX function prototypes */
	
	// int __fastcall__ clock_getres (clockid_t clock_id, struct timespec *res);
	if (clock_getres(CLOCK_REALTIME, &ts) != 0) 
		printf("Error with clock_getres()");
	else 
		printf ("clock_getres: s:%lu ns:%ld\n", (unsigned long)ts.tv_sec, ts.tv_nsec);
	
	// int __fastcall__ clock_gettime (clockid_t clock_id, struct timespec *tp);
	if (clock_gettime(CLOCK_REALTIME, &ts) != 0) 
		printf("Error with clock_gettime()");
	else 
		printf ("clock_gettime: s:%lu ns:%ld\n", (unsigned long)ts.tv_sec, ts.tv_nsec);

	// int __fastcall__ clock_settime (clockid_t clock_id, const struct timespec *tp);
	// Not implemented.



	return 0;
}
