package com.darwin.abilityservice.infrastructure.input.router;

import com.darwin.abilityservice.application.dto.AbilityRequest;
import com.darwin.abilityservice.application.dto.AbilityResponse;
import com.darwin.abilityservice.application.handler.IAbilityHandler;
import com.darwin.abilityservice.shared.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.darwin.abilityservice.infrastructure.util.Routes.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class AbilityRouter {
    private final IAbilityHandler abilityHandler;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = ABILITY_RESOURCE,
                    method = RequestMethod.POST,
                    beanClass = IAbilityHandler.class,
                    beanMethod = "create",
                    operation = @Operation(
                            summary = "Crea una nueva capacidad",
                            operationId = "create",
                            requestBody = @RequestBody(
                                    description = "Capacidad a crear",
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = AbilityRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Operación exitosa",
                                            content = @Content(schema = @Schema(implementation = AbilityResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error en la solicitud",
                                            content = @Content
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "El id de la tecnología no existe",
                                            content = @Content
                                    ),
                                    @ApiResponse(
                                            responseCode = "409",
                                            description = "El id de la tecnología está duplicándose",
                                            content = @Content
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = ABILITY_RESOURCE,
                    method = RequestMethod.GET,
                    beanClass = IAbilityHandler.class,
                    beanMethod = "paginate",
                    operation = @Operation(
                            summary = "Lista las capacidades de forma paginada",
                            operationId = "paginate",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Operación exitosa",
                                            content = @Content(
                                                    array = @ArraySchema(schema = @Schema(implementation = AbilityResponse.class)
                                                    ))
                                    )
                            },
                            parameters = {
                                    @Parameter(in = ParameterIn.QUERY, name = Pagination.PAGE_PARAM, example = Pagination.DEFAULT_PAGE),
                                    @Parameter(in = ParameterIn.QUERY, name = Pagination.SIZE_PARAM, example = Pagination.DEFAULT_SIZE),
                                    @Parameter(in = ParameterIn.QUERY, name = Pagination.SORT_ASCENDING, example = Pagination.DEFAULT_ASCENDING),
                                    @Parameter(in = ParameterIn.QUERY, name = Pagination.SORT_PROPERTY, example = "name")
                            }
                    )
            ),
            @RouterOperation(
                    path = ABILITY_RESOURCE_ID,
                    method = RequestMethod.GET,
                    beanClass = IAbilityHandler.class,
                    beanMethod = "findById",
                    operation = @Operation(
                            summary = "Obtiene una capacidad por su ID",
                            operationId = "findById",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Operación exitosa",
                                            content = @Content(schema = @Schema(implementation = AbilityResponse.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "ID no encontrado",
                                            content = @Content
                                    ),
                            },
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id", example = "1")
                            }
                    )
            ),
            @RouterOperation(
                    path = ABILITY_RESOURCE_ID_EXISTS,
                    method = RequestMethod.GET,
                    beanClass = IAbilityHandler.class,
                    beanMethod = "existsById",
                    operation = @Operation(
                            summary = "Consulta si existe una capacidad por su ID",
                            operationId = "existsById",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Operación exitosa",
                                            content = @Content(schema = @Schema(implementation = Boolean.class))
                                    )
                            },
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id", example = "1")
                            }
                    )
            ),
    })
    public RouterFunction<ServerResponse> abilityRoutes() {
        return route()
                .POST(ABILITY_RESOURCE, abilityHandler::create)
                .GET(ABILITY_RESOURCE, abilityHandler::paginate)
                .GET(ABILITY_RESOURCE_ID, abilityHandler::findById)
                .GET(ABILITY_RESOURCE_ID_EXISTS, abilityHandler::existsById)
                .build();
    }
}
