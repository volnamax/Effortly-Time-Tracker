void processDocuments(const std::vector<std::string> &filenames,
                      const std::vector<std::string> &filenames_out,
                      int start_index, int end_index,
                      std::mutex &io_mutex) {
    for (int i = start_index; i < end_index; ++i) {
        // Загрузка и обработка документа
        std::vector<Document> documents = readDocumentsFromCSV(filenames[i]);
        standardizeDocumentLengths(documents);

        // Выполнение иерархической кластеризации
        std::vector<Cluster> clusters = hierarchicalClustering(documents, MAX_DEPTH);

        // Синхронизированное сохранение результатов
        std::lock_guard<std::mutex> lock(io_mutex);
        saveClustersToJson(clusters, documents, filenames_out[i]);
    }
}