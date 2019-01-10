package utoronto.saturn.app;

public interface LoadingListener {
    void notifyLoadingStarted();
    void notifyLoadingFinished();
    void notifyProgress(double progress);
}
