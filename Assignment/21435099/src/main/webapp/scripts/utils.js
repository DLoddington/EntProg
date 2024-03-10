$('#searchType').change(function() {
   $('#searchId').parent().toggleClass('hide', this.value != '2' );
});

$('#searchType').change(function() {
   $('#searchName').parent().toggleClass('hide', this.value != '3' );
});