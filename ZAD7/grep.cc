#include <thread>
#include <future>
#include <vector>
#include <iostream>
#include <fstream>
#include <locale>
#include <list>
#include <codecvt>

// funkcja wywoływana przez wątki
void list_grep(std::list<std::string> filenames, std::wstring word,
               std::promise<unsigned int> &len_promise) {
    unsigned int count = 0;
    size_t size = filenames.size();
    for (size_t i = 0; i < size; i++) {
        std::string filename = filenames.front();
        filenames.pop_front();
        std::locale loc("pl_PL.UTF-8");
        std::wfstream file(filename);
        file.imbue(loc);
        std::wstring line;
        while (getline(file, line)) {
            for (auto pos = line.find(word, 0);
                 pos != std::string::npos;
                 pos = line.find(word, pos + 1))
                count++;
        }
    }
    len_promise.set_value(count); // zapisuję wartość licznika w obietnicy
}

int main() {
    std::ios::sync_with_stdio(false);
    std::locale loc("pl_PL.UTF-8");
    std::wcout.imbue(loc);
    std::wcin.imbue(loc);

    std::wstring word;
    std::getline(std::wcin, word);

    std::wstring s_file_count;
    std::getline(std::wcin, s_file_count);
    int file_count = std::stoi(s_file_count);

    std::list<std::string> filenames{};

    std::wstring_convert<std::codecvt_utf8<wchar_t>, wchar_t> converter;

    // określam liczbę wątków, np. 3
    int thread_count = 3;
    // deklaruję wektor zawierający listy z nazwami plików
    std::vector<std::list<std::string>> lists_filenames;
    // tworzę tablicę z obietnicami dla poszczególnych wątków
    std::promise<unsigned int> promises[thread_count];
    // tworzę tymczasową listę z nazwami plików
    std::list<std::string> lista;
    // tworzę licznik informujący, ile co najwyżej plików ma się znajdować
    // w każdej liście
    int list_count;
    if (file_count % thread_count == 0) list_count = file_count / thread_count;
    else list_count = file_count / thread_count + 1;

    for (int file_num = 0; file_num < file_count; file_num++) {
        std::wstring w_filename;
        std::getline(std::wcin, w_filename);
        std::string s_filename = converter.to_bytes(w_filename);

        // podział plików z danymi na listy dla poszczególnych wątków
        lista.push_back(s_filename);
        if (lista.size() == (size_t) list_count || file_num == file_count - 1) {
            lists_filenames.push_back(lista);
            lista.clear();
        }
    }
    // tworzę tablicę z wątkami
    std::thread threads[thread_count];
    // uruchamiam moje wątki
    for (size_t thread_num = 0;
         thread_num < lists_filenames.size(); thread_num++) {
        threads[thread_num] = std::thread{
                [&lists_filenames, word, &promises, thread_num] {
                    list_grep(lists_filenames[thread_num], word,
                              promises[thread_num]);
                }};
    }
    // czekam z wątkiem głównym na zakończenie uruchomionych wątków
    for (size_t thread_num = 0;
         thread_num < lists_filenames.size(); thread_num++) {
        threads[thread_num].join();
    }

    // zliczam wyniki odbierane z wątków
    unsigned int count = 0;
    for (size_t i = 0; i < lists_filenames.size(); i++) {
        count += promises[i].get_future().get();
    }

    std::wcout << count << std::endl;
}
