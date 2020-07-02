#include<stdio.h>
#include <termios.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <time.h>

FILE *fp;
int empty_square_number = 25;
int combo = 0;
int max_combo = 0;
int move_count = 0;
unsigned int score = 0;
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

////////////////////////////////////////keyboard//////////////////////////////////////////

void init_keyboard() {
    tcgetattr(0, &initial_settings);
    new_settings = initial_settings;
    new_settings.c_lflag &= ~ICANON;
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

////////////////////////////////////////record/////////////////////////////////////////

typedef struct RECORD {
    char r_name[10];
    char r_result[10];
    int r_score;
    long r_second;
    int r_move;
    int r_combo;
} RECORD;

void record_data(char *game_result, struct timespec begin) {
    fp = fopen("record.txt", "a+");
    char name[10];
    struct timespec now;
    clock_gettime(CLOCK_MONOTONIC, &now);
    close_keyboard();
    printf("Name(10):\n");
    scanf("%9s", name);
    fprintf(fp, "%s\t%s\t%d\t%ld\t%d\t%d\n", name, game_result, score, (now.tv_sec - begin.tv_sec), move_count,
            max_combo);
    init_keyboard();
    fclose(fp);
}

int compare_record(const void *a, const void *b) {
    RECORD record1 = *(RECORD *) a;
    RECORD record2 = *(RECORD *) b;

    int result1_int = strcmp(record1.r_result, "win");
    int result2_int = strcmp(record2.r_result, "win");
    if (result1_int == result2_int) {
        if (record1.r_score == record2.r_score) {
            if (record1.r_second == record2.r_second) {
                if (record1.r_move == record2.r_move) {
                    if (record1.r_combo == record2.r_combo) {
                        return 0;
                    }
                    return record2.r_combo - record1.r_combo;
                } else {
                    return record1.r_move - record2.r_move;
                }
            } else {
                return (int) (record1.r_second - record2.r_second);
            }
        } else {
            return record2.r_score - record1.r_score;
        }
    } else {
        return result2_int - result1_int;
    }
}

//////////////////////////////////////// game /////////////////////////////////////////

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
    printf("Score: %d\n", score);
    printf("Max Combo: %d\t", max_combo);
    printf("Combo: %d   \n", combo);

}

void add_empty_square(int row, int col) {
    game_board[row][col] = 0;
    empty_square_pointer[empty_square_number] = &game_board[row][col];
    empty_square_number++;
}

void generate_num_at_random_pos() {
    int random_empty_index = rand() % empty_square_number;
    int two_or_four = rand() % 2;
    *(empty_square_pointer[random_empty_index]) = two_or_four ? 2 : 4;
    empty_square_number--;
    memcpy(&empty_square_pointer[random_empty_index], &empty_square_pointer[random_empty_index + 1],
           sizeof(unsigned int *) * (empty_square_number - random_empty_index));
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

int merge(int row, int col, int search_row, int search_col, int row_dir, int col_dir, unsigned int value, int *merge_time) {
    if (search_col > 4 || search_col < 0 || search_row > 4 || search_row < 0) {
        return 0;
    } else if (game_board[search_row][search_col] == 0) {
        return merge(row, col, search_row + row_dir, search_col + col_dir, row_dir, col_dir, value, merge_time);
    } else {
        unsigned int temp = game_board[search_row][search_col];
        if (temp == value) {
            add_empty_square(search_row, search_col);
            game_board[row][col] = value * 2;
            score += value * 2;
            (*merge_time)++;
        } else if (value == 0) {
            add_empty_square(search_row, search_col);
            game_board[row][col] = temp;
            remove_empty_square(row, col);
        } else if (search_row == row + row_dir && search_col == col + col_dir) {
            return 0;
        } else {
            add_empty_square(search_row, search_col);
            game_board[row + row_dir][col + col_dir] = temp;
            remove_empty_square(row + row_dir, col + col_dir);
        }
    }
    return 1;
}

unsigned int move(int row, int col, int row_dir, int col_dir, int *merge_time) {
    int new_row = row + row_dir;
    int new_col = col + col_dir;
    unsigned int value = game_board[row][col];
    unsigned int is_moved = merge(row, col, new_row, new_col, row_dir, col_dir, value, merge_time);
    if (value == 0) {
        value = game_board[row][col];
        if (merge(row, col, new_row, new_col, row_dir, col_dir, value, merge_time) == 1) {
            move(new_row, new_col, row_dir, col_dir, merge_time);
            is_moved = 1;
        }
    } else {
        is_moved = is_moved | move(new_row, new_col, row_dir, col_dir, merge_time);
    }
    return is_moved;
}

unsigned int command(char input, int *merge_time) {
    int row_dir = 0, col_dir = 0;
    unsigned int is_moved = 0;
    if (input == 'A' || input == 'a') {
        col_dir = 1;
        for (int i = 0; i < 5; i++) {
            is_moved = is_moved | move(i, 0, row_dir, col_dir, merge_time);
        }
    } else if (input == 'D' || input == 'd') {
        col_dir = -1;
        for (int i = 0; i < 5; i++) {
            is_moved = is_moved | move(i, 4, row_dir, col_dir, merge_time);
        }
    } else if (input == 'S' || input == 's') {
        row_dir = -1;
        for (int i = 0; i < 5; i++) {
            is_moved = is_moved | move(4, i, row_dir, col_dir, merge_time);
        }
    } else if (input == 'W' || input == 'w') {
        row_dir = 1;
        for (int i = 0; i < 5; i++) {
            is_moved = is_moved | move(0, i, row_dir, col_dir, merge_time);
        }
    }
    return is_moved;
}

unsigned int check_gameover(struct timespec begin) {
    struct timespec now;
    clock_gettime(CLOCK_MONOTONIC, &now);
    if (now.tv_sec - begin.tv_sec > 600) {
        return 0;
    } else if (empty_square_number == 0) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (game_board[i][j] == game_board[i + 1][j] || game_board[i][j] == game_board[i][j + 1]) {
                    return 0;
                }
            }
        }
        for (int j = 0; j < 4; j++) {
            if (game_board[4][j] == game_board[4][j + 1]) {
                return 0;
            }
        }
        return 1;
    } else {
        return 0;
    }
}

unsigned int check_clear() {
    for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
            if (game_board[i][j] == 2048) {
                return 1;
            }
        }
    }
    return 0;
}

void print_menu() {
    printf("\n");
    printf("1. Game Start\n");
    printf("2. How to\n");
    printf("3. Ranking\n");
    printf("q. Exit\n");
}


void init_game() {
    empty_square_number = 25;
    combo = 0;
    max_combo = 0;
    move_count = 0;
    score = 0;
    for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 5; j++) {
            game_board[i][j] = 0;
            empty_square_pointer[i * 5 + j] = &(game_board[i][j]);
        }
    }
}

int main(void) {
    srand((unsigned)time(NULL));
    init_keyboard();
    char input;
    system("clear");
    while (1) {
        system("clear");
        print_menu();
        input = _getch();
        if (input == '1') {
            init_game();
            struct timespec begin, now;
            clock_gettime(CLOCK_MONOTONIC, &begin);
            generate_num_at_random_pos();
            generate_num_at_random_pos();
            print_gameboard(game_board);
            while (input != 'q') {
                if (_kbhit()) {
                    input = _getch();
                    _putch(input);
                    int merge_time = 0;
                    if (command(input, &merge_time)) {
                        move_count++;
                        if (merge_time == 0) {
                            combo = 0;
                        } else {
                            combo += merge_time;
                            if (max_combo < combo) {
                                max_combo = combo;
                            }
                        }
                        generate_num_at_random_pos();
                        print_gameboard(game_board);
                    }
                }
                clock_gettime(CLOCK_MONOTONIC, &now);
                printf("\r Time : %ld   ", 600 - (now.tv_sec - begin.tv_sec));
                fflush(stdout);
                if (check_clear() == 1) {
                    system("clear");
                    gotoxy(0, 0);
                    printf("clear\n");
                    record_data("win", begin);
                    break;
                }
                if (check_gameover(begin)) {
                    system("clear");
                    gotoxy(0, 0);
                    printf("game over\n");
                    record_data("lose", begin);
                    break;
                }
                usleep(10000);
            }
        } else if (input == '2') {
            system("clear");
            printf("게임 시작시 2개의 2또는 4가 랜덤한 곳에서 생성된다.\n"
                   "방향키를 누르면 해당하는 방향으로 게임판에 있는 숫자를 전부 몰게 된다.\n"
                   "이동하면서 같은 숫자를 만날 경우 합쳐지며, 빈 자리 중 한칸에 랜덤하게 2 또는 4가 생성된다.\n"
                   "이를 반복하여 2048 타일을 만들면 게임 클리어\n"
                   "2048을 만들기 전 더이상 숫자를 몰 수 없는 경우(16칸이 꽉 차있으면서 인접한 두 칸이 같지\n"
                   "않을 경우) 게임오버\n");
            printf("press any key");
            fflush(stdout);
            _getch();
        } else if (input == '3') {
            system("clear");
            if((fp = fopen("record.txt", "r+"))==NULL){
                FILE *fp2=fopen("record.txt","a");
                fclose(fp2);
                fp = fopen("record.txt", "r+");
            }
            fseek(fp, 0, SEEK_SET);
            RECORD records[1000];
            int record_index = 0;
            while (!feof(fp)) {
                RECORD record = {};
                fscanf(fp, "%s\t%s\t%d\t", record.r_name, record.r_result, &record.r_score);
                fscanf(fp, "%ld\t%d\t%d\n", &record.r_second, &record.r_move, &record.r_combo);
                records[record_index] = record;
                record_index++;
            }
            qsort(records, record_index, sizeof(RECORD), compare_record);

            printf("rank    name          result\tscore\ttime(sec)\tmove\tmax_combo\n");
            for (int index = 0; index < record_index; index++) {
                RECORD record = records[index];
                if(record.r_second !=0){
                    printf("%d.\t", index + 1);
                    printf("%-10s\t%-4s\t%d\t", record.r_name, record.r_result, record.r_score);
                    printf("%ld\t\t%d\t%d\n", record.r_second, record.r_move, record.r_combo);
                }
            }
            fclose(fp);
            printf("press any key");
            fflush(stdout);
            _getch();
        } else if (input == 'q') {
            printf("exit");
            break;
        } else {
            printf("Invalid input");
        }
    }

}
