
std::vector<Point> kMeansParallel(const std::vector<Point> &points, int k, int numThreads) {
    int n = points.size();
    std::vector<Point> centroids(k);
    std::vector<int> assignments(n);

    // Инициализация центроидов
    for (int i = 0; i < k; ++i) {
        centroids[i] = points[rand() % n];
    }

    std::atomic<bool> changed;
    do {
        changed = false;
        std::vector<std::thread> threads;
        int chunkSize = n / numThreads;


        // Назначение точек кластерам
        for (int i = 0; i < numThreads; ++i) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? n : start + chunkSize;
            threads.emplace_back(assignPointsToClusters,
                                 std::ref(points), std::ref(centroids),
                                 std::ref(assignments), start, end,
                                 std::ref(changed));
        }

        for (auto &thread: threads) {
            thread.join();
        }
        threads.clear();

        // Пересчет центроидов
        std::vector<Point> newCentroids(k, Point{0, 0});
        std::vector<int> counts(k, 0);

        for (int i = 0; i < numThreads; ++i) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? n : start + chunkSize;
            threads.emplace_back(calculateCentroids,
                                 std::ref(points), std::ref(assignments),
                                 std::ref(newCentroids), std::ref(counts),
                                 k, start, end);
        }

        for (auto &thread: threads) {
            thread.join();
        }

        for (int j = 0; j < k; ++j) {
            if (counts[j] > 0) {
                newCentroids[j].x /= counts[j];
                newCentroids[j].y /= counts[j];
            }
        }
        centroids = newCentroids;
    } while (changed);

    return centroids;
}

struct Point {
    double x, y;
};

double distance(const Point &p1, const Point &p2) {
    return sqrt(pow(p2.x - p1.x, 2) + pow(p2.y - p1.y, 2));
}

void assignPointsToClusters(const std::vector<Point> &points,
                            const std::vector<Point> &centroids,
                            std::vector<int> &assignments,
                            int start, int end,
                            std::atomic<bool> &changed) {
    for (int i = start; i < end; ++i) {
        int bestCluster = 0;
        double bestDist = std::numeric_limits<double>::max();
        for (int j = 0; j < centroids.size(); ++j) {
            double dist = distance(points[i], centroids[j]);
            if (dist < bestDist) {
                bestDist = dist;
                bestCluster = j;
            }
        }
        if (assignments[i] != bestCluster) {
            assignments[i] = bestCluster;
            changed = true;
        }
    }
}

void calculateCentroids(const std::vector<Point> &points,
                        const std::vector<int> &assignments,
                        std::vector<Point> &newCentroids,
                        std::vector<int> &counts,
                        int k, int start, int end) {
    for (int i = start; i < end; ++i) {
        newCentroids[assignments[i]].x += points[i].x;
        newCentroids[assignments[i]].y += points[i].y;
        counts[assignments[i]]++;
    }
}