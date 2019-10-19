package dreamcompany.mappings;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MappingsInitializer {

    private static final String ROOT_PACKAGE_NAME = "dreamcompany";

    public static void initMappings(ModelMapper mapper) {

        String configureMappingsMethodName = IHaveCustomMappings.class.getDeclaredMethods()[0].getName();

        getClassesWithCustomMappings()
                .forEach(klass -> invokeMethodFromClass(klass, configureMappingsMethodName, mapper));
    }

    private static List<Class<?>> getClassesWithCustomMappings() {

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AssignableTypeFilter(IHaveCustomMappings.class));

        var candidates = scanner.findCandidateComponents(MappingsInitializer.ROOT_PACKAGE_NAME);

        return candidates
                .stream()
                .map(BeanDefinition::getBeanClassName)
                .map(MappingsInitializer::getClassFromName)
                .filter(Objects::nonNull)
                .filter(IHaveCustomMappings.class::isAssignableFrom)
                .collect(Collectors.toList());
    }

    private static Class<?> getClassFromName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void invokeMethodFromClass(Class<?> klass, String methodName, Object... params) {

        System.out.println();
        try {
            Method method = klass.getDeclaredMethod(methodName, ModelMapper.class);
            var obj = klass.getConstructor().newInstance();
            method.invoke(obj, params);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
