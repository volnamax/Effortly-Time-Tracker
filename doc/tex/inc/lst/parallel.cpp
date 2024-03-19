void parallelDocClustering(const std::vector<std::string> &filenames,
                           const std::vector<std::string> &filenames_out,
                           int num_threads) {
    int count_f = filenames.size();
    std::mutex io_mutex;

    // Расчет размера подгруппы для каждого потока
    int chunk_size = count_f / num_threads;
    std::thread threads[num_threads]; // Массив потоков

    int start_index = 0;
    for (int i = 0; i < num_threads; ++i) {
        int end_index = start_index + chunk_size + (i < count_f % num_threads ? 1 : 0);

        // Создание потока для обработки подгруппы документов
        threads[i] = std::thread(processDocuments, std::ref(filenames), std::ref(filenames_out),
                                 start_index, end_index, std::ref(io_mutex));
        start_index = end_index;
    }

    // Дождаться завершения всех потоков
    for (int i = 0; i < num_threads; ++i) {
        threads[i].join();
    }
}