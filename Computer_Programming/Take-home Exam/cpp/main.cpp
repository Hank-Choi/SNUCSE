#include <iostream>
#include <string>
#include <utility>
#include <set>
#include <vector>
#include <tuple>

using namespace std;

/* =======START OF PRIME-RELATED HELPERS======= */
/*
 * The code snippet below AS A WHOLE does the primality
 * test and integer factorization. Feel free to move the
 * code to somewhere more appropriate to get your codes
 * more structured.
 *
 * You don't have to understand the implementation of it.
 * But if you're curious, refer to the sieve of Eratosthenes
 *
 * If you want to just use it, use the following 2 functions.
 *
 * 1) bool is_prime(int num):
 *     * `num` should satisfy 1 <= num <= 999999
 *     - returns true if `num` is a prime number
 *     - returns false otherwise (1 is not a prime number)
 *
 * 2) multiset<int> factorize(int num):
 *     * `num` should satisfy 1 <= num <= 999999
 *     - returns the result of factorization of `num`
 *         ex ) num = 24 --> result = { 2, 2, 2, 3 }
 *     - if `num` is 1, it returns { 1 }
 */

const int PRIME_TEST_LIMIT = 999999;
int sieve_of_eratosthenes[PRIME_TEST_LIMIT + 1];
bool sieve_calculated = false;

void make_sieve() {
    sieve_of_eratosthenes[0] = -1;
    sieve_of_eratosthenes[1] = -1;
    for(int i=2; i<=PRIME_TEST_LIMIT; i++) {
        sieve_of_eratosthenes[i] = i;
    }
    for(int i=2; i*i<=PRIME_TEST_LIMIT; i++) {
        if(sieve_of_eratosthenes[i] == i) {
            for(int j=i*i; j<=PRIME_TEST_LIMIT; j+=i) {
                sieve_of_eratosthenes[j] = i;
            }
        }
    }
    sieve_calculated = true;
}

bool is_prime(int num) {
    if (!sieve_calculated) {
        make_sieve();
    }
    return sieve_of_eratosthenes[num] == num;
}

multiset<int> factorize(int num) {
    if (!sieve_calculated) {
        make_sieve();
    }
    multiset<int> result;
    while(num > 1) {
        result.insert(sieve_of_eratosthenes[num]);
        num /= sieve_of_eratosthenes[num];
    }
    if(result.empty()) {
        result.insert(1);
    }
    return result;
}

/* =======END OF PRIME-RELATED HELPERS======= */

/* =======START OF STRING LITERALS======= */
/* Use this code snippet if you want */

const string MAXIMIZE_GAIN = "Maximize-Gain";
const string MINIMIZE_LOSS = "Minimize-Loss";
const string MINIMIZE_REGRET = "Minimize-Regret";

/* =======END OF STRING LITERALS======= */


/* =======START OF TODOs======= */

pair<int, int> number_fight(int a, int b) {
    multiset<int> fa = factorize(a);
    multiset<int> fb = factorize(b);
    set<int> set_a(fa.begin(),fa.end());
    set<int> set_b(fb.begin(),fb.end());
    vector<int> fg;
    fg.resize(set_a.size()+set_b.size());
    auto iter = set_intersection(set_a.begin(),set_a.end(),set_b.begin(),set_b.end(),fg.begin());
    fg.erase(iter,fg.end());
    int g = 1;
    for(int g_prime:fg){
        g *= g_prime;
    }
    return pair<int, int>(a/g,b/g);
}

pair<int, int> number_vs_number(int a, int b) {
    pair<int, int> ff = number_fight(a,b);
    pair<int, int> nf;
    pair<int, int> fn;
    pair<int, int> nn = pair<int, int>(a,b);
    bool a_fight;
    bool b_fight;
    if(a%7 == 0)
        nf = pair<int,int>(a-(a-ff.first)/2,(b-(a-ff.first)/2)<1 ? 1 : b-(a-ff.first)/2);
    else
        nf = pair<int,int>(ff.first,b);
    if(b%7 == 0)
        fn = pair<int,int>(a-(b-ff.second)/2 < 1 ? 1 : a-(b-ff.second)/2,b-(b-ff.second)/2);
    else
        fn = pair<int,int>(a,ff.second);
    bool a_cond1 = ff.first >= nf.first ? true : false;
    bool a_cond2 = fn.first >= nn.first ? true : false;
    bool b_cond1 = ff.second >= fn.second ? true : false;
    bool b_cond2 = nf.second >= nn.second ? true : false;
    if(a_cond1==a_cond2)
        a_fight=a_cond1;
    else if(b>a)
        a_fight=true;
    else
        a_fight=false;
    if(b_cond1==b_cond2)
        b_fight=b_cond1;
    else if(a>b)
        b_fight=true;
    else
        b_fight=false;
    if(a_fight && b_fight)
        return ff;
    else if(a_fight && !b_fight)
        return fn;
    else if(!a_fight && b_fight)
        return nf;
    else
        return nn;
    // TODO 1-2
}

pair<multiset<int>, multiset<int>> player_battle(
    string type_a, multiset<int> a, string type_b, multiset<int> b
) {
    int max_a[a.size()],min_a[a.size()],max_b[b.size()],min_b[b.size()];
    vector<int> abc;
    int max_a_index, max_b_index, min_a_index, min_b_index;
    int list_a[a.size()],list_b[b.size()];
    for(int i=0 ; i<a.size() ; i++) {
        max_a[i] = INT32_MIN;
        min_a[i] = INT32_MAX;
    }
    for(int i=0 ; i<b.size() ; i++) {
        max_b[i] = INT32_MIN;
        min_b[i] = INT32_MAX;
    }
    pair<int, int> table[a.size()][b.size()];
    auto a_iter = a.begin();
    auto b_iter = b.begin();
    int a_index,b_index;
    int i=0,j=0;
    while(a_iter != a.end()){
        list_a[i]=*a_iter;
        while(b_iter != b.end()){
            list_b[j]=*b_iter;
            pair<int,int> vs = number_vs_number(*a_iter,*b_iter);
            pair<int,int> simulation = pair<int,int>(vs.first-*a_iter,vs.second-*b_iter);
            if(simulation.first > max_a[i])
                max_a[i] = simulation.first;
            if(simulation.first < min_a[i])
                min_a[i] = simulation.first;
            if(simulation.second > max_b[j])
                max_b[j] = simulation.second;
            if(simulation.second < min_b[j])
                min_b[j]=simulation.second;
            table[i][j] = vs;
            ++b_iter;
            ++j;
        }
        b_iter = b.begin();
        j=0;
        ++a_iter;
        ++i;
    }
    min_a_index=0;
    int min_loss_a = min_a[0];
    for (int i = 0; i < a.size(); i++) {
        if (min_a[i] > min_loss_a) {
            min_a_index = i;
            min_loss_a = min_a[i];
        }
    }
    if(type_a == MINIMIZE_LOSS)
        a_index = min_a_index;
    else{
        max_a_index = 0;
        int max_a_val = max_a[0];
        int second_max_a;
        for (int i = 0; i < a.size(); i++) {
            if (max_a[i] > max_a_val) {
                max_a_val = max_a[i];
                max_a_index = i;
            }
        }
        for (int i = 0; i < a.size(); i++) {
            if (i != max_a_index) {
                if (max_a[i] > second_max_a) {
                    second_max_a = max_a[i];
                }
            }
        }
        if( type_a == MAXIMIZE_GAIN)
            a_index = max_a_index;
        else {
            if (second_max_a - min_a[max_a_index] > max_a_val - min_loss_a)
                a_index = min_a_index;
            else if (second_max_a - min_a[max_a_index] < max_a_val - min_loss_a)
                a_index = max_a_index;
            else if (max_a_index < min_a_index)
                a_index = max_a_index;
            else
                a_index = min_a_index;
        }
    }
    min_b_index=0;
    int min_loss_b = min_b[0];
    for (int i = 0; i < b.size(); i++) {
        if (min_b[i] > min_loss_b) {
            min_b_index = i;
            min_loss_b = min_b[i];
        }
    }
    if(type_b == MINIMIZE_LOSS)
        b_index = min_b_index;
    else{
        max_b_index = 0;
        int max_b_val = max_b[0];
        int second_max_b;
        for (int i = 0; i < b.size(); i++) {
            if (max_b[i] > max_b_val) {
                max_b_val = max_b[i];
                max_b_index = i;
            }
        }
        for (int i = 0; i < b.size(); i++) {
            if (i != max_b_index) {
                if (max_b[i] > second_max_b) {
                    second_max_b = max_b[i];
                }
            }
        }
        if( type_b == MAXIMIZE_GAIN)
            b_index = max_b_index;
        else {
            if (second_max_b - min_b[max_b_index] > max_b_val - min_loss_b)
                b_index = min_b_index;
            else if (second_max_b - min_b[max_b_index] < max_b_val - min_loss_b)
                b_index = max_b_index;
            else if (max_b_index < min_b_index)
                b_index = max_b_index;
            else
                b_index = min_b_index;
        }
    }
    //min index만 찾으면 됨
    //min index랑 max index랑 같으면 무조건 그거, 다르면 min index의 최대값 구해서 regret 계산하면 됨
    multiset<int> result_a = a;
    multiset<int> result_b = b;
    result_a.erase(list_a[a_index]);
    result_a.insert(table[a_index][b_index].first);
    result_b.erase(list_b[b_index]);
    result_b.insert(table[a_index][b_index].second);

    return pair<multiset<int>, multiset<int>>(result_a,result_b);
}

pair<multiset<int>, multiset<int>> player_vs_player(
    string type_a, multiset<int> a, string type_b, multiset<int> b
) {
    pair<multiset<int>, multiset<int>> temp = pair<multiset<int>, multiset<int>>(a, b);
    pair<multiset<int>, multiset<int>> result = player_battle(type_a, a, type_b, b);
    while (temp != result) {
        temp = result;
        result = player_battle(type_a,a,type_b,b);
    }
    return result;
}

bool winner(pair<string,multiset<int>> &player1, pair<string,multiset<int>> &player2, int index1, int index2){
    pair<multiset<int>, multiset<int>>result= player_vs_player(player1.first,player1.second,player2.first,player2.second);
    int player1_score = 0;
    int player2_score = 0;
    for(int k:result.first)
        player1_score += k;
    for(int k:result.second)
        player2_score += k;
    if(player2_score>player1_score)
        return false;
    else
        return true;
}

int tournament(vector<pair<string, multiset<int>>> players) {
    vector<int> player_num = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    while (true) {
        vector<pair<string, multiset<int>>> result;
        vector<int> result_num;
        auto even_iter = players.begin();
        auto odd_iter = players.begin() + 1;
        auto num_even_iter = player_num.begin();
        auto num_odd_iter = player_num.begin() +1;
        int even_index = 0;
        if (odd_iter == players.end())
            return player_num.at(0);
        else if (winner(*even_iter, *odd_iter, even_index, even_index + 1)) {
            result.push_back(*even_iter);
            result_num.push_back(*num_even_iter);
        }
        else{
            result.push_back(*odd_iter);
            result_num.push_back(*num_odd_iter);
        }
        even_iter += 2;
        while (even_iter != players.end()) {
            odd_iter += 2;
            if(odd_iter == players.end()){
                result.push_back(*even_iter);
                result_num.push_back(*num_even_iter);
                break;
            }
            else if(winner(*even_iter, *odd_iter, even_index, even_index + 1)){
                result.push_back(*even_iter);
                result_num.push_back(*num_even_iter);
            }
            else{
                result.push_back(*odd_iter);
                result_num.push_back(*num_odd_iter);
            }
            even_iter += 2;
        }
        players = result;
        player_num = result_num;
    }
}

int steady_winner(vector<pair<string, multiset<int>>> players) {
    int win_count[16]={0,};
    int winner_index;
    int winner_count;
    for(int i = 0;i<players.size();i++) {
        win_count[(tournament(players)+i)%players.size()]++;
        players.push_back(*players.begin());
        players.erase(players.begin());
    }
    winner_count=win_count[0];
    for(int k = 0;k<players.size();k++){
        if(winner_count<win_count[k]){
            winner_index = k;
            winner_count = win_count[k];
        }
    }
    return winner_index;
}

/* =======END OF TODOs======= */

/* =======START OF THE MAIN CODE======= */
/* Please do not modify the code below */

typedef pair<string, multiset<int>> player;

player scan_player() {
    multiset<int> numbers;
    string player_type; int size;
    cin >> player_type >> size;
    for(int i=0;i<size;i++) {
        int t; cin >> t; numbers.insert(t);
    }
    return make_pair(player_type, numbers);
}

void print_multiset(const multiset<int>& m) {
    for(int number : m) {
        cout << number << " ";
    }
    cout << endl;
}

int main() {
    int question_number; cin >> question_number;
    if (question_number == 1) {
        int a, b; cin >> a >> b;
        tie(a, b) = number_fight(a, b);
        cout << a << " " << b << endl;
    } else if (question_number == 2) {
        int a, b; cin >> a >> b;
        tie(a, b) = number_vs_number(a, b);
        cout << a << " " << b << endl;
    } else if (question_number == 3 || question_number == 4) {
        auto a = scan_player();
        auto b = scan_player();
        multiset<int> a_, b_;
        if (question_number == 3) {
            tie(a_, b_) = player_battle(
                a.first, a.second, b.first, b.second
            );
        } else {
            tie(a_, b_) = player_vs_player(
                a.first, a.second, b.first, b.second
            );
        }
        print_multiset(a_);
        print_multiset(b_);
    } else if (question_number == 5 || question_number == 6) {
        int num_players; cin >> num_players;
        vector<player> players;
        for(int i=0;i<num_players;i++) {
            players.push_back(scan_player());
        }
        int winner_id;
        if (question_number == 5) {
            winner_id = tournament(players);
        } else {
            winner_id = steady_winner(players);
        }
        cout << winner_id << endl;
    }
    return 0;
}
/* =======END OF MAIN CODE======= */