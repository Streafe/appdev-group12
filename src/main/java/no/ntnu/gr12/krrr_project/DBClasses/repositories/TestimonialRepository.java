package no.ntnu.gr12.krrr_project.DBClasses.repositories;

import no.ntnu.gr12.krrr_project.DBClasses.models.Testimonial;
import org.springframework.data.repository.CrudRepository;


/**
 * @author Thomas Ystenes
 * Interface for TestimonialRepository
 */
public interface TestimonialRepository extends CrudRepository<Testimonial, String> {
}
