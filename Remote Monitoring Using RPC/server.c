#include <rpc/rpc.h>
#include <sys/types.h>
#include "data.h"
#include <stdlib.h>
#include <time.h>
#include <stdio.h>
#include <sys/sysinfo.h>
#include <sys/resource.h>
char* result;
void getCommandResult(char*);
char **data_1(option)long *option;{
    struct tm *timeptr;
    time_t clock;
    static char *ptr;
    static char s[100];
    static char err[] = "Invalid Response \0";
    struct sysinfo sys_info;
    struct rusage ru;
    getrusage(RUSAGE_SELF, &ru);
    struct timeval tv;
    gettimeofday(&tv, NULL);
    clock = time(0);
    timeptr = localtime(&clock);
    double megabyte = 1024 * 1024;
    switch(*option)
    {
        case 1:
            strftime(s,100,"%A, %B %d, %Y",timeptr);
            ptr=s;
            break;
            
        case 2:
            strftime(s,100,"%T",timeptr);
            ptr=s;
            break;
            
        case 3:
            strftime(s,100,"%A, %B %d, %Y - %T",timeptr);
            ptr=s;static char s[100];

            break;
            
        case 4:
            getCommandResult("top -bn 2 -d 1 | grep '^Cpu.s.' | tail -n 1 | awk 'OMFT \"%.1f\" {print $2}' | rev | cut -c4- | rev");
            ptr=result;
            break;
        case 5:
            sysinfo(&sys_info); 
            sprintf(s, "Total Mem: %ldk\tFree: %ldk\n", sys_info.totalram/1024,
                                        sys_info.freeram/1024);
            ptr=s; // in MB
            break;
        case 6:
            sysinfo(&sys_info); 
            sprintf(s, "Load procs per mins: 1min(%f) 5min(%f) 15min(%f)\n",
          sys_info.loads[0]/65536.0, sys_info.loads[1]/65536.0, sys_info.loads[2]/65536.0);
            ptr=s;
            break;    
    
        default: 
            ptr=err;
            break;
    }
    
    return(&ptr);
}
void getCommandResult(char* input){
    FILE *pf;
    result = (char *)malloc(500);
    pf = popen(input, "r");
    if (!pf) {
        fprintf(stderr, "Can not open pf");
        return;
    }
    fgets(result, 100, pf);
}
