#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>

int main() {

    FILE *file;
    FILE *src;
    FILE *dest;
    char ch;

    file = fopen("message.txt", "w");
    if (file == NULL) {
        printf("Error creating file.\n");
        return 1;
    }
    fprintf(file, "Hello! This file was created using C.\n");
    fclose(file);

    file = fopen("message.txt", "r");
    if (file == NULL) {
        printf("File not found.\n");
    } else {
        while ((ch = fgetc(file)) != EOF)
            putchar(ch);
        fclose(file);
    }

    chmod("message.txt", 0600);

    src = fopen("message.txt", "r");
    dest = fopen("copy_message.txt", "w");

    if (src == NULL || dest == NULL) {
        printf("Error copying file.\n");
        return 1;
    }

    while ((ch = fgetc(src)) != EOF)
        fputc(ch, dest);

    fclose(src);
    fclose(dest);

    printf("\nFile copied successfully.\n");

    return 0;
}