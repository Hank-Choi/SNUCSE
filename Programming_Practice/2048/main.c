#include<stdio.h>
#include <termios.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <time.h>

int empty_square_number = 25;
unsigned int game_board[5][5] = {0,};
unsigned int *empty_square_pointer[25] = {
        &(game_board[0][0]), &(game_board[0][1]), &(game_board[0][2]), &(game_board[0][3]), &(game_board[0][4]),
        &(game_board[1][0]), &(game_board[1][1]), &(game_board[1][2]), &(game_board[1][3]), &(game_board[1][4]),
        &(game_board[2][0]), &(game_board[2][1]), &(game_board[2][2]), &(game_board[2][3]), &(game_board[2][4]),
        &(game_board[3][0]), &(game_board[3][1]), &(game_board[3][2]), &(game_board[3][3]), &(game_board[3][4]),
        &(game_board[4][0]), &(game_board[4][1]), &(game_board[4][2]), &(game_board[4][3]), &(game_board[4][4])
};

static struct termios initial_settings, new_settings;
static int peek_character = -1;

////////////////////////////////////////keyboard/////////////////////////////////////////

void init_keyboard() {
    tcgetattr(0, &initial_settings);
    new_settings = initial_settings;
    new_settings.c_lflag &= ~ICANON;
//    new_settings.c_lflag |= ECHO;
    new_settings.c_lflag &= ~ECHO;
    new_settings.c_cc[VMIN] = 1;
    new_settings.c_cc[VTIME] = 0;
    tcsetattr(0, TCSANOW, &new_settings);
}

void close_keyboard() {
    tcsetattr(0, TCSANOW, &initial_settings);
}

int _kbhit() {
    unsigned char ch;
    int nread;
    if (peek_character != -1) return 1;
    new_settings.c_cc[VMIN] = 0;
    tcsetattr(0, TCSANOW, &new_settings);
    nread = read(0, &ch, 1);
    new_settings.c_cc[VMIN] = 1;
    tcsetattr(0, TCSANOW, &new_settings);
    if (nread == 1) {
        peek_character = ch;
        return 1;
    }
    return 0;
}


int _getch() {
    char ch;
    if (peek_character != -1) {
        ch = peek_character;
        peek_character = -1;
        return ch;
    }
    read(0, &ch, 1);
    return ch;
}

int _putch(int c) {
    putchar(c);
    fflush(stdout);
    return c;
}

void gotoxy(int x, int y) {
    printf("\033[%d;%df", y, x);
    fflush(stdout);
}

////////////////////////////////////////keyboard/////////////////////////////////////////


void print_gameboard(unsigned int (*game_board)[5]) {
    gotoxy(0, 0);
    printf("-------------------------------\n");
    for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
            printf("|%4d ", game_board[i][j]);
        }
        printf("|\n");
        printf("-------------------------------\n");
    }
    printf("W: 상, S: 하, A: 좌, D: 우, Q: 종료\n");
}

void add_empty_square(int row,int col){
        game_board[row][col] = 0;
        empty_square_pointer[empty_square_number] = &game_board[row][col];
        empty_square_number++;
}

void remove_empty_square(int update_row, int update_col) {
    for (int i = 0; i < empty_square_number; i++) {
        if (empty_square_pointer[i] == &game_board[update_row][update_col]) {
            memcpy(&empty_square_pointer[i], &empty_square_pointer[i + 1],
                   sizeof(unsigned int *) * (empty_square_number - i));
            empty_square_number--;
            break;
        }
    }
}

int merge(int row, int col, int search_row, int search_col, int row_dir, int col_dir, unsigned int value) {
    if (search_col > 4 || search_col < 0 || search_row > 4 || search_row < 0) {
        return 0;
    } else if (game_board[search_row][search_col] == 0) {
        return merge(row, col, search_row + row_dir, search_col + col_dir, row_dir, col_dir, value);
    } else {
        unsigned int temp = game_board[search_row][search_col];
        if (temp == value) {
            add_empty_square(search_row,search_col);
            game_board[row][col] = value * 2;
        } else if (value == 0) {
            add_empty_square(search_row,search_col);
            game_board[row][col] = temp;
            remove_empty_square(row, col);
        }
        else if(search_row == row+row_dir && search_col == col+col_dir){
            return 0;
        }
        else {
            add_empty_square(search_row,search_col);
            game_board[row + row_dir][col + col_dir] = temp;
            remove_empty_square(row + row_dir, col + col_dir);
        }
    }
    return 1;
}

unsigned int move(int row, int col, int row_dir, int col_dir) {
    int new_row = row + row_dir;
    int new_col = col + col_dir;
    unsigned int value = game_board[row][col];
    unsigned int is_moved = merge(row, col, new_row, new_col, row_dir, col_dir, value);
    if (value == 0) {
        value = game_board[row][col];
        if (merge(row, col, new_row, new_col, row_dir, col_dir, value) == 1) {
            move(new_row, new_col, row_dir, col_dir);
            is_moved=1;
        }
    } else {
        is_moved = is_moved | move(new_row, new_col, row_dir, col_dir);
    }
    return is_moved;
}

void generate_2_at_random_pos() {
    int random_empty_index = rand() % empty_square_number;
    *(empty_square_pointer[random_empty_index]) = 2;
    empty_square_number--;
    memcpy(&empty_square_pointer[random_empty_index], &empty_square_pointer[random_empty_index + 1],
           sizeof(unsigned int *) * (empty_square_number - random_empty_index));
}

unsigned int command(char input) {
    int row_dir = 0, col_dir = 0;
    unsigned int is_moved=0;
    if (input == 'A' || input == 'a') {
        col_dir = 1;
        for (int i = 0; i < 5; i++) {
            is_moved=is_moved|move(i, 0, row_dir, col_dir);
        }
    } else if (input == 'D' || input == 'd') {
        col_dir = -1;
        for (int i = 0; i < 5; i++) {
            is_moved = is_moved|move(i, 4, row_dir, col_dir);
        }
    } else if (input == 'S' || input == 's') {
        row_dir = -1;
        for (int i = 0; i < 5; i++) {
            is_moved=is_moved|move(4, i, row_dir, col_dir);
        }
    } else if (input == 'W' || input == 'w') {
        row_dir = 1;
        for (int i = 0; i < 5; i++) {
            is_moved=is_moved|move(0, i, row_dir, col_dir);
        }
    }
    return is_moved;
}


int main(void) {
    init_keyboard();
    char input = 'c';
    system("clear");
    struct timespec begin, now;
    clock_gettime(CLOCK_MONOTONIC, &begin);
    generate_2_at_random_pos();
    print_gameboard(game_board);
    while (input != 'q') {
        if (_kbhit()) {
            input = _getch();
            _putch(input);
            if (command(input)) {
                generate_2_at_random_pos();
                print_gameboard(game_board);
                if (empty_square_number == 0) {
                    system("clear");
                    gotoxy(0, 0);
                    printf("game over\n");
                    return 0;
                }
            }
        }
        clock_gettime(CLOCK_MONOTONIC, &now);
        printf("\r Time : %ld", now.tv_sec - begin.tv_sec);
        fflush(stdout);
        usleep(10000);
    }

}



//TODO:게임오버 조건 변경
//TODO:콤보 표시
//TODO:기록 저장
