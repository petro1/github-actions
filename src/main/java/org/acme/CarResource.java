package org.acme;

import io.smallrye.common.constraint.NotNull;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dao.CarService;
import org.acme.dto.CarDTO;
import org.acme.dto.FormData;
import org.apache.tika.Tika;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Path("/car")
public class CarResource {

    private static final List<String> allowedMimeType = List.of("xlsx");

    @Inject
    CarService carService;

    @Schema(type = SchemaType.STRING, format = "binary")
    private static class UploadItemSchema {
    }

    @GET
    @Path("/all")
    public List<CarDTO> findAllCars() {
        return carService.findAllCars();
    }

    @GET
    @Path("/brand/{brand}")
    public List<CarDTO> findByBrand(@PathParam("brand") String brand) {
        return carService.findByBrand(brand);
    }

    @GET
    @Path("/model/{model}")
    public List<CarDTO> findByModel(@PathParam("model") String model) {
        return carService.findByModel(model);
    }

    @POST
    @Path("/new")
    public void create(CarDTO carDTO) {
        carService.createCar(carDTO);
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam("id") long id, @QueryParam("model") String model, @QueryParam("brand") String brand) {
        carService.updateCar(id, model, brand);
    }

    @POST
    @Operation(summary = "Upload a file with extension: xlsx")
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @RestForm("file")
            @Schema(implementation = FormData.UploadItemSchema.class)
            FileUpload file) throws Exception {

        if (file.fileName() == null || file.fileName().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Uploaded file is empty").build();
        }

        if (file.contentType() == null || file.contentType().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Content Type is empty").build();
        }

        if (!isExtensionAllowed(file.fileName().toLowerCase())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Content Type not allowed").build();
        }

        if (!validateContentType(file)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Content Type invalid").build();
        }

        return Response.ok().status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/delete/{id}")
    public void delete(@PathParam("id") Long id) {
        carService.deleteCar(id);
    }

    private boolean isExtensionAllowed(final String fileName) {
        final Optional<String> hasEnding = allowedMimeType.stream().filter(fileName::endsWith).findFirst();

        return hasEnding.isPresent();
    }

    private boolean validateContentType(final FileUpload fileUpload) throws IOException {
        Tika tika = new Tika();
        String mediaType = tika.detect(fileUpload.uploadedFile());
        return fileUpload.contentType().equals(mediaType);
    }

}
