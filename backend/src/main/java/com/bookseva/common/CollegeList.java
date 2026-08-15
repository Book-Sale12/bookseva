package com.bookseva.common;

import java.util.Set;

/**
 * Canonical list of predefined college/university names.
 * Must be kept in sync with the frontend list in colleges.js.
 * The sentinel value "Other" is intentionally excluded — the backend
 * should never receive raw "Other"; it should receive the user-typed value.
 */
public final class CollegeList {

    private CollegeList() {}

    public static final Set<String> PREDEFINED = Set.of(
        "CDAC (Centre for Development of Advanced Computing)",
        "Savitribai Phule Pune University",
        "University of Mumbai",
        "University of Delhi",
        "Anna University",
        "Bangalore University",
        "Osmania University",
        "Rajasthan University",
        "Gujarat University",
        "Nagpur University (RTMNU)",
        "Shivaji University",
        "Amity University",
        "Symbiosis International University",
        "VIT University",
        "SRM Institute of Science and Technology",
        "Manipal Academy of Higher Education",
        "Lovely Professional University",
        "Chandigarh University",
        "IIT Bombay",
        "IIT Delhi",
        "IIT Madras",
        "IIT Kanpur",
        "NIT Pune",
        "NIT Trichy",
        "BITS Pilani"
    );
}
