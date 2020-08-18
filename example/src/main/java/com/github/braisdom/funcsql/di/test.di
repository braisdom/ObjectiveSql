
-- comment
enum age_groups {
    180: '3 months',
    390: '6 months'
}

dataset MemberProducts {

}

-- comment
dataset Member(show_mobile: boolean) {
    projection [
        id,
        member_no,
        if(name is null, 'unknown', name) name,
        gender,
        mobile present show_mobile.length() > 0,
        age_groups[birthday] age_group,
        birthday,
        status
    ],
    predicate (status = 1 && birthday > purchase_begin),
    join [left(MemberProducts mp, mp.member_id = member.id)],
    from [pumper.member member],
    group [],
    order [-id, +member_no],
    having ()
}

-- comment
query purchased_member(sku: array, purchase_begin: date, purchase_end: date) {

}