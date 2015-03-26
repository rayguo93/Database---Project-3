# Database---Project-3
Database Project 3

## 1) get top tags
input limit
```sql
select tag_name, count(*) from ad_tag_map
group by tag_name
order by count(*) desc
limit 3;
```

## 2) select the username and number of unique ad views of the user with the most view count on all his ads
```sql
select username, s from customer
inner join
(select creator_id, sum(unique_view_count) s from advertisement
group by creator_id
order by s desc
limit 1) A
on A.creator_id = customer.uid;
```

## 3) select all convo ids and titles with messages that were sent after 12 December 2015 
input date
```sql
select distinct convo.convo_id, convo.title from 
(select * from message
where message.created >= to_timestamp('12 Dec 2014', 'DD Mon YYYY')) M
inner join convo
on convo.convo_id = M.convo_id;
```

## 4) select all usernames of users who posted at least two items with a price greater than 200
input price
```sql
select distinct c1.username from 
customer c1, advertisement a1, advertisement a2
where c1.uid = a1.creator_id and a1.price > 200 and c1.uid = a2.creator_id and a2.price > 200 and a1.ad_id != a2.ad_id;
```

## 5) select username, ads_count of users who put up more than 1 advertisements (active users)
input x advertisements and y messages
```sql
(select username, email from customer
inner join
(select uid from message
group by uid
having count(*) > 2) M
on customer.uid = M.uid)
intersect
(select username, email from customer
inner join
(select creator_id from advertisement
group by creator_id
having count(*) > 1) A
on customer.uid = A.creator_id);
```

## 6) quit
