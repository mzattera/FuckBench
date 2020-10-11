/*
 * Dan Gookin
 * https://c-for-dummies.com
 * January, 2020
 */
#include <stdio.h>
#include <stdlib.h>

#define ROOMS 20
#define SIZE 32

enum { EMPTY,WUMPUS,BAT,PIT };
enum { FALSE, TRUE };

/* populates a room in the labyrinth with
   the given character 'c'
   player position 'pp' is avoided */
void prep_room( int laby[], int c, int pp )
{
	int r;

	while(1)
	{
		r = rand() % ROOMS;
		/* keep the player's room empty */
		if( r==pp )
			continue;
		/* leave when an empty room is found */
		if( laby[r]==EMPTY )
			break;
	}
	/* place object c into the room */
	laby[r] = c;
}

/* process input */
char getinput(void)
{
	char input[SIZE];
	char *r;

	/* obtain and test input */
	r = fgets(input,SIZE,stdin);
	if( r==NULL )
	{
		fprintf(stderr,"Error on input, exiting\n");
		exit(1);
	}

	/* filter out the ? character */
	if( input[0]=='?' )
		return('?');
	/* make the returned character uppercase */
	return(input[0] & 0xdf);
}

/* determine whether the player died
   player has already moved into a room
   return TRUE - player alive
   return FALSE - player has died */
int player_alive(int laby[],int pp)
{
	/* BAT is handled after the function returns */
	if( laby[pp]==EMPTY || laby[pp]==BAT )
	{
		/* return TRUE, player still alive */
		return(TRUE);
	}

	/* otherwise, output the appropriate death message */
	if( laby[pp]==WUMPUS )
	{
		puts(" You have met the Wumpus!");
		puts(" He has eaten you in a most painful manner!");
	}
	if( laby[pp]==PIT )
	{
		puts(" You have fallen down a bottomless pit!");
	}

	/* because the player was eaten by the wumpus
	   or fell down a pit, return dead: FALSE==dead */
	return(FALSE);
}

/* you shot and missed the wumpus!
   this function is called from the
   following function, arrow_loose() */
void shot_missed(int laby[],int pp)
{
	int right,left,behind;

	/* calculate nearby room element offsets */
	right = (pp+ROOMS-1)%ROOMS;
	left = (pp+ROOMS+1)%ROOMS;
	behind = (pp+ROOMS/2)%ROOMS;

	/* 75% chance the wumpus moves */
	if( ( rand()%40) < 30 )
	{
		/* remove the wumpus from the current room */
		if( laby[left]==WUMPUS )
			laby[left]=EMPTY;
		else if( laby[right]==WUMPUS )
			laby[right]=EMPTY;
		else
			laby[behind]=EMPTY;
		/* reset the wumpus */
		prep_room(laby,WUMPUS,pp);
		/* the wumpus will not be in the same room
		   as the player */
		puts(" The wumpus has fled!");
	}
	else
	{
		puts(" You hear a meanacing growl!");
	}
	puts(" ---");
}

/* an arrow is shot at the wumpus
   return TRUE if the wumpus was killed,
   false otherwise */
int arrow_loose(int laby[], int pp, char direction)
{
	int right,left,behind;

	/* calculate nearby room element offsets */
	right = (pp+ROOMS-1)%ROOMS;
	left = (pp+ROOMS+1)%ROOMS;
	behind = (pp+ROOMS/2)%ROOMS;

	/* determine if the wumpus is nearby
	   and potentially has been shot */
	switch( direction )
	{
		case 'R':
			if( laby[right]==WUMPUS )
			{
				return(TRUE);
			}
			/* if you shot and missed ... */
			if( laby[left]==WUMPUS || laby[behind]==WUMPUS )
			{
				puts(" Your arrow goes into the wrong room!");
				shot_missed(laby,pp);
			}
			break;
		case 'L':
			if( laby[left]==WUMPUS )
			{
				return(TRUE);
			}
			/* if you shot and missed ... */
			if( laby[right]==WUMPUS || laby[behind]==WUMPUS )
			{
				puts(" Your arrow goes into the wrong room!");
				shot_missed(laby,pp);
			}
			break;
		case 'B':
			if( laby[behind]==WUMPUS )
			{
				return(TRUE);
			}
			/* if you shot and missed ... */
			if( laby[right]==WUMPUS || laby[left]==WUMPUS )
			{
				puts(" Your arrow goes into the wrong room!");
				shot_missed(laby,pp);
			}
			break;
		case 'U':
			if( laby[right]==WUMPUS || laby[left]==WUMPUS || laby[behind]==WUMPUS )
			{
				puts(" Your arrow is wasted!");
				shot_missed(laby,pp);
			}
			break;
		default:
			/* they should never get here */
			puts("Error: 1101");
	}
	return(FALSE);
}

/* a giant bat moves the player into an empty room */
int bat_move(int laby[],int pp)
{
	int r;

	puts(" A giant bat has picked you up!");
	/* remove the bat */
	laby[pp] = EMPTY;
	/* reset the bat to a new location */
	prep_room( laby, BAT, pp );

	/* plop down the player somewhere safe */
	while(1)
	{
		r = rand() % ROOMS;
		/* don't move back to the same room */
		if( r == pp )
			continue;
		/* only move to an empty room */
		if( laby[r]==EMPTY )
			break;
	}
	puts(" You've been flown to a new location in the labyrinth!");
	return(r);
}

/* main function */
int main()
{
	int labyrinth[ROOMS];
	struct hunter {
		int position,arrows,alive,won;
	} player;
	int x,done;
	int right,left,behind;

	/**************/
	/* initialize */
	/**************/
	
	/* seed randomizer */
	unsigned seed;
	printf ("Please enter an integer (0-65535) for the random number generator: ");	
	scanf ("%u", &seed);	
	srand(seed);
	puts ("\n");	

	/* initialize labyrinth */
	for(x=0;x<ROOMS;x++)
	{
		labyrinth[x] = EMPTY;
	}
		/* set player defaults */
	player.position = 0;
	player.arrows = 5;
	player.alive = TRUE;
	player.won = FALSE;
		/* set bottomless pit position 1 */
	prep_room( labyrinth, PIT, player.position );
		/* set bottomless pit position 2 */
	prep_room( labyrinth, PIT, player.position );
		/* set bat position 1 */
	prep_room( labyrinth, BAT, player.position );
		/* set bat position 2 */
	prep_room( labyrinth, BAT, player.position );
		/* set wumpus position */
	prep_room( labyrinth, WUMPUS, player.position );
		/* exit condition */
	done = FALSE;

		/* initial message */
	puts("Hunt the Wumpus");
	puts("Dan Gookin, C For Dummies\n");
	puts("Each room in the labyrinth has three doors. To win the game,");
	puts("you must shoot an arrow through a door to kill the Wumpus!");
	puts("If you enter the room with the Wumpus, you die! If you enter");
	puts("a room with a pit, you die! If you enter a room with a giant");
	puts("bat, you're carried to another (empty) room in the labyrinth.");
	puts("The labyrinth holds two giant bats, two bottomless pits, and");
	puts("one Wumpus! Good luck.\n");
	puts("Type 'H)elp' for help\n");

	/*************/
	/* main loop */
	/*************/
	while(!done)
	{
		/* exit the loop if the player has died
		   or the player shot the wumpus */
		if( player.alive==FALSE || player.won==TRUE )
			break;

		/* initialize directions/element numbers */
		right = (player.position+ROOMS-1)%ROOMS;
		left = (player.position+ROOMS+1)%ROOMS;
		behind = (player.position+ROOMS/2)%ROOMS;

		/* always give the location */
			/* add one to player position for human eyeballs */
		printf(" You are in chamber %d of the labyrinth.\n",player.position+1);
		puts(" ---");
		puts(" Doors are to your R)ight and L)eft, as well as B)ehind you");
		printf(" You have %d A)rrows in your quiver\n",player.arrows);
		/* display "sense" of items in nearby rooms */
		if( labyrinth[right]==PIT || labyrinth[left]==PIT || labyrinth[behind]==PIT)
			puts(" You feel a cold wind blowing from a nearby cavern");
		if( labyrinth[right]==WUMPUS || labyrinth[left]==WUMPUS || labyrinth[behind]==WUMPUS)
			puts(" You smell something terrible nearby");
		if( labyrinth[right]==BAT || labyrinth[left]==BAT || labyrinth[behind]==BAT)
			puts(" You hear a rustling");

		/* begin command interpreter */
		printf("Command: ");
		switch( getinput() )
		{
			/* right */
			case 'R':
				puts(" You go through the right door...");
				player.position = right;
				player.alive = player_alive(labyrinth,player.position);
				/* if player has died, the BAT wasn't in the room */
				if( labyrinth[player.position]==BAT )
					player.position = bat_move(labyrinth,player.position);
				break;
			/* left */
			case 'L':
				puts(" You go through the left door...");
				player.position = left;
				player.alive = player_alive(labyrinth,player.position);
				/* if player has died, the BAT wasn't in the room */
				if( labyrinth[player.position]==BAT )
					player.position = bat_move(labyrinth,player.position);
				break;
			/* behind */
			case 'B':
				puts(" You back through the door behind you...");
				player.position = behind;
				player.alive = player_alive(labyrinth,player.position);
				/* if player has died, the BAT wasn't in the room */
				if( labyrinth[player.position]==BAT )
					player.position = bat_move(labyrinth,player.position);
				break;
			/* arrow */
			case 'A':
				printf(" Point your bow through the R)ight or L)eft doors, or B)ehind you? ");
				/* read input */
				switch( getinput() )
				{
					case 'R':
						puts(" -> You fire right!");
						player.won = arrow_loose(labyrinth,player.position,'R');
						break;
					case 'L':
						puts(" -> You fire left!");
						player.won = arrow_loose(labyrinth,player.position,'L');
						break;
					case 'B':
						puts(" -> You fire behind you!");
						player.won = arrow_loose(labyrinth,player.position,'B');
						break;
					default:
						puts(" -> Where did that one go?");
						player.won = arrow_loose(labyrinth,player.position,'U');
				}
				if(player.won)
					break;
				/* decrease the number of arrows */
				player.arrows--;
				/* if the player is out of arrows, game over */
				if( player.arrows==0 )
				{
					puts(" Out of arrows, your fate is sealed! So sad!");
					player.alive = FALSE;
					done=TRUE;
				}
				break;
			/* help */
			case 'H':
			case '?':
				puts(" Commands are: ?) H)elp , Q)uit");
				puts("  Go through the R)ight door; L)eft door; door B)ehind you");
				puts("  Draw your A)rrow, then fire R)ight, L)eft, or B)ehind");
				puts(" ---");
				break;
			/* quit */
			case 'Q':
				done = TRUE;
				puts(" Goodbye!");
				break;
			/* the user just presses Enter */
			case '\n':
				break;
			default:
				puts(" ? Unrecognized command");
		}
	}

	/* play summary detail */
	if( player.won==TRUE )
		puts(" You have killed the Wumpus!");
	if( player.alive==FALSE )
		/* player has died */
		puts(" You have died!");

	puts(" _Game over_");

	return(0);
}