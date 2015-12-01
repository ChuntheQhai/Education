//
//  main.c
//  OperatingSystem-Assignment2
//
//  Created by Khoo Chun Qhai on 11/17/15.
//  Copyright © 2015 Chunify. All rights reserved.
//

#include <stdio.h>
#include <pthread.h>
#include <semaphore.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

#define TRUE 1
#define FALSE 0
#define SUCCESS 1
#define FAIL 0
#define NOT_END_OF_FILE 0
#define END_OF_FILE 1
#define MAX_BUF_SIZE 1024
#define SIZE_OF_BUF_ELEMS 16
#define OUTPUT_FILE_NAME "output.txt"


sem_t len;
char n[MAX_BUF_SIZE];
pthread_mutex_t mutexReader = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mutexWriter = PTHREAD_MUTEX_INITIALIZER;


struct rowData **sharedBufferData;


struct rowData {
    char *buffer[MAX_BUF_SIZE];
    int availability;
};

typedef struct Circular{
    int count;
}Circular;

/* Shared Buffer */
struct Circular cb;
char *writerBuffer;



/* Global Variables */
FILE *fpReader;
FILE *fpWriter;
int readerCounter = -1; // ReaderCounter shows NULL
int writerCounter = 0; // ReaderCounter shows NULL
int itemIndex = -1;

/* Prototypes */
void *writeFileThread();
void *readFileThread();
void pushDataToSharedBuffer(int index, char* data);
void popDataFromSharedBuffer();




/* Methods for Circular SharedBuffer */
void pushDataToSharedBuffer(int index, char* data)
{
    memcpy(sharedBufferData[index]->buffer, data,sizeof(data));
    sharedBufferData[index]->availability = TRUE;
    cb.count++;
}

void popDataFromSharedBuffer(void)
{
    cb.count--;
}


void *readFileThread() {
    char *buffer = (char *) malloc(sizeof(char) *MAX_BUF_SIZE);
    if (buffer == NULL) {
        printf("Error allocating memory of line buffer.");
        exit(1);
    }
    
    while(fgets(buffer, MAX_BUF_SIZE, fpReader) != NULL) {
        pthread_mutex_lock( &mutexReader );
        printf("buffer: %s\n", buffer);
        
        if(itemIndex < SIZE_OF_BUF_ELEMS - 1)
            itemIndex++;
        else
            itemIndex = 0;

        pushDataToSharedBuffer(itemIndex,buffer);
        pthread_mutex_unlock( &mutexReader );
    }
    
    fclose(fpReader);
    
    if (buffer)
        free(buffer);
    
    pthread_exit(NULL);
}


void *writeFileThread()
{
    pthread_mutex_lock(&mutexWriter);

    fpWriter = fopen(OUTPUT_FILE_NAME, "a");
    if (fpWriter == NULL) {
        printf("Cannot write access to the file.");
    }

    while(cb.count >= 0) {

        if(writerCounter <= 15){
            popDataFromSharedBuffer();
            fprintf(fpWriter,"%s",(char*)sharedBufferData[writerCounter]->buffer);
        }else
            cb.count--;

        writerCounter++;
    }
 

    fclose(fpWriter);
    pthread_mutex_unlock(&mutexWriter);
    pthread_exit(NULL);
}

int main(int argc, char ** argv) {
    /* Global iniatialization */
    
    
    fpWriter = fopen(OUTPUT_FILE_NAME,"w");
    fclose(fpWriter);
    
    pthread_mutex_init(&mutexReader, NULL);
    pthread_mutex_init(&mutexWriter, NULL);

    /* Shared Buffer Initialization */
    
        // Allocate 16 pointers, an array
        sharedBufferData =  (struct rowData**) malloc(SIZE_OF_BUF_ELEMS * sizeof(struct rowData * ));
    
    
        // Allocate 16 structs and have array point to them
        int i;
        for (i = 0; i < SIZE_OF_BUF_ELEMS; i++)
        {
            sharedBufferData[i] = *(struct rowData**) malloc(sizeof(struct rowData *));
        }
    
        int j;
        for (j = 0; j < SIZE_OF_BUF_ELEMS; j++) {
            strcpy((char*)sharedBufferData[j]->buffer,"");
            sharedBufferData[j]->availability = FALSE;
        }
    
    /* Circular Buffer Pointer Initialization */
    cb.count = -1;
    
    
    
    fpReader = fopen(argv[1], "r");
    
    if (fpReader == NULL) {
        printf("File doesn't exists");
    }
    
    
    pthread_t readerThreads[3];

    
    int k;
    for (k = 0; k < 3; k++)
    {
        pthread_create(&readerThreads[k], NULL, &readFileThread, NULL);
    }
    
    int l;
    for (l = 0; l < 3; l++){
        pthread_join(readerThreads[l], NULL);
    }

    
    int m;
    for(m = 0; m < SIZE_OF_BUF_ELEMS; m++){
        printf("%s\n",(char*)sharedBufferData[m]);
    }

    
    pthread_t writerThreads[3];
    
    int n;
    for (n = 0; n < 3; n++)
    {
        pthread_create(&writerThreads[n], NULL, &writeFileThread, NULL);
    }
    
    int o;
    for (o = 0; o < 3; o++)
    {
        pthread_join(writerThreads[o], NULL);
    }

    return 0;
}


/* Instruction */
/* 1. Open the input and output files sepecified on the command line */

/* 2. Create the required mutexes and/or semaphores */

/* 3. Create framework for the message passing buffer.*/

/* 4. When it receives a termination message from one of the reader threads,
 the parent will close the input file and terminate the remaining reader threads. */

/* 5. When it receives a termination message from one of the writer threads,
 the parent will close the output file and terminate the remaining writer threads*/

/* 6. The reader thread code will provide following FUNC: */
/* a) Reader will allocate an input buffer up to 1024 bytes (Is Bigger?) */
/* b) Reader will read the lines from input and insert them into buffer.
 Need malloc storage for line and to move the text from the buffer to new storage
 Need to insert string to shared buffer to perform concurrent access.
 if it read END-OF-FILE or ZERO bytes, the THREAD set shared end-of-flag and terminate.*/

/* 7. The writer thread code will provide following FUNC: */
/* a) The writer will read lines from the shared buffer and write them to output
 Detach buffer array and reset buffer pointer to NULL
 Need to free() storage for line after writting to output file*/
/* b) Writer thread get string from buffer and finds that there are no entries in buffer,
 If the end-of-file flag is set, the thread should be terminate.*/





