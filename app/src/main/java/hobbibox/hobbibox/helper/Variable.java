package hobbibox.hobbibox.helper;


public class Variable {
    private OnVariableChangedListener mOnVariableChangedListener;
    private boolean b1;

    public void setOnVariableChangedListener(OnVariableChangedListener listener) {
        mOnVariableChangedListener = listener;
    }

    public void reset(){
        b1 = false;
    }

    // Boxes and categories are loaded asynchronously, so once both call
    // do Event, the user is signed in
    public void doEvent() {
        if(b1){
            if (mOnVariableChangedListener != null)
                mOnVariableChangedListener.onVariableChanged();
        }
        else{
            b1 = true;
        }
    }

    public void noEvent(){
        mOnVariableChangedListener.noActivity();
    }
}
