package Application.Objects;
public class User {

    // '_','user_name','user_id','user_authority','email','discord_id'
    // user data:
    private String  tag_id,
                    user_name,
                    user_id,
                    email,
                    discord_id;
    private short user_authority;

    public String getUserName() { return user_name; }
    public String getUserId() { return user_id; }
    public short getUserAuthority() { return user_authority; }
    public String getEmail() { return email; }
    public String getDiscordId() { return discord_id; }

    // userdataFetchLevel: 0:empty | 1:tag_id+user_id+user_name | 2:full
    private short userdataFetchLevel = 0;


    public User(String tag_id, String user_name, String user_id, int user_authority, String email, String discord_id) {
        System.out.println("User instance created..");
        this.tag_id = tag_id;
        this.user_name = user_name;
        this.user_id = user_id;
        this.user_authority = (short)user_authority;
        this.email = email;
        this.discord_id = discord_id;

        checkUserdataFetchLevel();
    }
    public User(String tag_id, String user_name, String user_id) {
        this(tag_id, user_name, user_id, 0, "", "");
    }
    public User() {
        this("","","",0,"","");
    }

    private void checkUserdataFetchLevel() {
        if(tag_id != "" && user_id != "" && user_name != "") {
            if(email != "" && discord_id != "")
            {
                userdataFetchLevel = 2;
            }else{
                userdataFetchLevel = 1;
            }
        }else{
            userdataFetchLevel = 0;
        }
    }

    
}
