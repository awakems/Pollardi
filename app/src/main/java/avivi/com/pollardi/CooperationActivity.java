package avivi.com.pollardi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Void on 12.05.2015.
 */
public class CooperationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooperation);

        setTitle("Cooperation");

        final EditText text1 = (EditText) findViewById(R.id.editText);
        final EditText text2 = (EditText) findViewById(R.id.editText2);
        final EditText text3 = (EditText) findViewById(R.id.editText3);
        final EditText text4 = (EditText) findViewById(R.id.editText4);
        final EditText text5= (EditText) findViewById(R.id.editText5);
        final EditText text6 = (EditText) findViewById(R.id.editText6);
        final EditText text7 = (EditText) findViewById(R.id.editText7);
        final EditText text8 = (EditText) findViewById(R.id.editText8);
        final EditText text9 = (EditText) findViewById(R.id.editText9);

        Button button = (Button) findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = text1.getText().toString();
                String country = text2.getText().toString();
                String town = text3.getText().toString();
                String phone = text4.getText().toString();
                String salon_name = text5.getText().toString();
                String your_site = text6.getText().toString();
                String your_email = text7.getText().toString();
                String brand = text8.getText().toString();
                String avarage_price = text9.getText().toString();


                String to = "explo1d@mail.ru";
                String subject = country + " " + town + " " + name ;
                String message = name + " " + country + " " + town + "\n" +
                        "phone = " + phone + "\n" +
                        "salone name = " + salon_name + "\n" +
                        "site = " + your_site + "\n" +
                        "email = " + your_email + "\n" +
                        "brands in = " + brand + "\n" +
                        "avarage price = " + avarage_price;

                System.out.println(message + "???????????");




                if (!name.isEmpty()&&!country.isEmpty()&&!town.isEmpty()&&!your_email.isEmpty()){
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
                    email.putExtra(Intent.EXTRA_SUBJECT, subject);
                    email.putExtra(Intent.EXTRA_TEXT, message);

                    // need this to prompts email client only
                    email.setType("message/rfc822");

                    startActivity(Intent.createChooser(email, "Choose an Email client"));
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "заполните поля с * ", Toast.LENGTH_SHORT);
                    toast.show();
                }



            }
        });

    }
}
