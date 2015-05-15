#include <stdio.h>
#include <string.h>
#include <time.h>
#include <sys/types.h>
#include <rpc/rpc.h>
#include "data.h"

long get_response(void);


long get_response()
{
  long choice;

  printf("Menu: \n");
  printf("1. Current System Time(Date)\n");
  printf("2. Current System Time(Time)\n");
  printf("3. Current System Time(Date & Time)\n");
  printf("4. CPU Usage\n");
  printf("5. Memory Usage\n");
  printf("6. Load Procs per Min \n");
  printf("7. Quit \n");
  printf("Make a choice (1-7):");
  scanf("%ld", &choice);

  return(choice);
}


int main(int argc, char **argv)
{
  CLIENT *cl;
  char *server;
  char **sresult;
  char s[100];
  long response;
  long *lresult;

  if(argc != 2)
  {
	fprintf(stderr, "usage:%s hostname\n", argv[0]);
	exit(1);
  }

  server = argv[1];
  lresult = (&response);

  if((cl=clnt_create(server,DATA_PROG,DATA_VERS,"udp")) == NULL)
  {
	clnt_pcreateerror(server);
	exit(2);
  }
  
  response = get_response();
  
  while(response!=7)
  {
	if((sresult = data_1(lresult,cl)) == NULL)
	{
	  clnt_perror(cl,server);
	  exit(3);
	}

 	printf(" %s\n", *sresult);
	response = get_response();
  }

  clnt_destroy(cl);
  exit(0);
}
