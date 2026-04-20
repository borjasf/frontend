package segundum.pasarela.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RetrofitConfig {

    @Bean
    public RetrofitUsuarios retrofit() {
    	Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://usuarios:8080/api/")
                .addConverterFactory(JacksonConverterFactory.create()) 
                .build();

        return retrofit.create(RetrofitUsuarios.class);
    }

}