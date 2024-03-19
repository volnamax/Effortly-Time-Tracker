void hierarchicalClustering(const std::vector<std::vector<double>> &docs,
                   int depth,
                   int maxDepth,
                   std::vector<Cluster> &clusters) {
    if (depth == maxDepth) {
        Cluster cluster;
        cluster.docs = docs;
        cluster.calculateCentroid();
        clusters.push_back(cluster);
        return;
    }

    // Применяем k-средних для разделения на 2 кластера
    std::vector<std::vector<double>> centroids = kMeans(docs, 2);
    std::vector<std::vector<double>> cluster1Docs, cluster2Docs;

    for (const auto &doc: docs) {
        if (cosineDistance(doc, centroids[0]) < cosineDistance(doc, centroids[1])) {
            cluster1Docs.push_back(doc);
        }
        else {
            cluster2Docs.push_back(doc);
        }
    }

    // Рекурсивно разделяем каждый подкластер
    hierarchicalClustering(cluster1Docs, depth + 1, maxDepth, clusters);
    hierarchicalClustering(cluster2Docs, depth + 1, maxDepth, clusters);
}