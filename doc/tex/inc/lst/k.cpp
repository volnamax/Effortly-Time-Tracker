std::vector<std::vector<double>> kMeans(const std::vector<std::vector<double>> &docs, int k) {
    int n = docs.size(); //1
    std::vector<std::vector<double>> centroids(k, std::vector<double>(docs[0].size())); //2
    std::vector<int> assignments(n, 0); //3
    // Инициализация центроидов 
    for (int i = 0; i < k; ++i) { // 4
        centroids[i] = docs[rand() % n]; //5
    }
    bool changed; // 6
    do { // 7
        changed = false; // 8 
        // Назначение точек кластерам
        for (int i = 0; i < n; ++i) { //9
            double bestDist = -1.0; //10
            int bestCluster = 0; //11
            for (int j = 0; j < k; ++j) { //12
                double dist = cosineDistance(docs[i], centroids[j]);// 13
                if (dist > bestDist) {//14
                    bestDist = dist;//15
                    bestCluster = j;//16
                }
            }
            if (assignments[i] != bestCluster) { //17
                assignments[i] = bestCluster;//18
                changed = true;//19
            }
        }
        // Обновление центроидов
        std::vector<int> counts(k, 0); //20
        std::vector<std::vector<double>> newCentroids(k, std::vector<double>(docs[0].size(), 0.0));//21
        for (int i = 0; i < n; ++i) {//22
            for (size_t j = 0; j < docs[i].size(); ++j) {//23
                newCentroids[assignments[i]][j] += docs[i][j];//24
            }
            counts[assignments[i]]++;//25
        }
        for (int j = 0; j < k; ++j) {//26
            if (counts[j] != 0) {//27
                for (size_t m = 0; m < newCentroids[j].size(); ++m) {//28
                    newCentroids[j][m] /= counts[j];//29
                }
            }
            else {//30
                newCentroids[j] = centroids[j]; //31
            }
        }
        centroids = newCentroids;//32
    } while (changed);//33

    return centroids;//34
}