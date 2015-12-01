CC = gcc
CFLAGS = -Wall
LDFLAGS=-pthread
OBJ = main.o

%.o: %.c $(DEPS)
	$(CC) $(CFLAGS) -c -o $@ $< -ggdb

main: $(OBJ)
	gcc $(CFLAGS) -o $@ $^ $(LDFLAGS) -ggdb