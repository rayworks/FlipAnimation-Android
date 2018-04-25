package blog.droidsonroids.pl.blogpost.viewmodel;

public class CardViewModel {
    // other fields...
    private boolean backLayoutVisible;

    public boolean isBackLayoutVisible() {
        return backLayoutVisible;
    }

    public CardViewModel setBackLayoutVisible(boolean backLayoutVisible) {
        this.backLayoutVisible = backLayoutVisible;
        return this;
    }
}
