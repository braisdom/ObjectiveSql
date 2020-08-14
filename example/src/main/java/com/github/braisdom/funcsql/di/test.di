
-- comment
enum age_groups {
    180: '3 months',
    390: '6 months';
}

-- comment
set member {
    projection [
        id,
        member_no,
        if(name is null, 'unknown', name) name,
        gender,
        mobile,
        age_groups[birthday] age_group,
        birthday,
        status
    ],
    predicate (status = 1 && birthday > purchase_begin),
    join (pumper.member_products mp, mp.member_id = member.id),
    from (pumper.member),
    group [],
    order [id, member_no],
    having ();
} mem

-- comment
query purchased_member(sku: array, purchase_begin: date, purchase_end: date) {

}