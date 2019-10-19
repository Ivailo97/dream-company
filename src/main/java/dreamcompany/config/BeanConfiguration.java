package dreamcompany.config;

import dreamcompany.domain.entity.Team;
import dreamcompany.domain.model.binding.TeamEditBindingModel;
import dreamcompany.domain.model.service.OfficeServiceModel;
import dreamcompany.domain.model.service.TeamServiceModel;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.mappings.MappingsInitializer;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class BeanConfiguration {

    private static ModelMapper mapper;

    static {
        mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);


  //    mapper.createTypeMap(TeamServiceModel.class, TeamEditBindingModel.class)
  //            .addMapping(
  //                    TeamServiceModel::getName,
  //                    TeamEditBindingModel::setName
  //            )
  //            .addMapping(
  //                    TeamServiceModel::getTeamLeader,
  //                    (model, value) -> model.setTeamLeader(new String[]{((UserServiceModel) value).getId(),
  //                            String.format("%s %s", ((UserServiceModel) value).getFirstName(),
  //                                    ((UserServiceModel) value).getLastName())})
  //            )
  //            .addMapping(
  //                    TeamServiceModel::getEmployees,
  //                    (model, value) -> {

  //                        Set<UserServiceModel> usersServiceModels = (Set<UserServiceModel>) value;
  //                        List<String[]> users = new ArrayList<>();

  //                        usersServiceModels.forEach(e -> {
  //                            String[] user = new String[2];
  //                            user[0] = e.getId();
  //                            user[1] = String.format("%s %s", e.getFirstName(), e.getLastName());
  //                            users.add(user);
  //                        });
  //                        model.setEmployees(users);
  //                    }
  //            )
  //            .addMapping(
  //                    TeamServiceModel::getId,
  //                    TeamEditBindingModel::setId
  //            )
  //            .addMapping(
  //                    TeamServiceModel::getOffice,
  //                    (model, value) -> model.setOffice(((OfficeServiceModel) value).getId())
  //            );

        // MappingsInitializer.initMappings(mapper);
        //mapper.validate();
    }

    @Bean
    public ModelMapper modelMapper() {
        return mapper;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
