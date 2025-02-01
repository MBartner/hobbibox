package hobbibox.hobbibox;

import android.content.Context;

import hobbibox.hobbibox.helper.Variable;
import hobbibox.hobbibox.service.FirebaseService;


public class DependencyFactory {
    private static FirebaseService sFirebaseService;
    private static Variable sUserLoaded;

    public static FirebaseService getFirebaseService(Context context){
        if(sFirebaseService == null){
            sFirebaseService = new FirebaseService();
        }
        return sFirebaseService;
    }
    
    public static Variable getUserLoaded(){
        if(sUserLoaded == null){
            sUserLoaded = new Variable();
        }
        return sUserLoaded;
    }
}
