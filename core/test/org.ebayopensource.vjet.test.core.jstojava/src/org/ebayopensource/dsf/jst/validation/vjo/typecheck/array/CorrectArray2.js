vjo.ctype('org.ebayopensource.dsf.jst.validation.vjo.typecheck.array.CorrectArray2')
.protos({
x : [] //< public String[]
})
.props({
//> public void main(String[] args)
main:function(args)
{
var obj1 = new this.vj$.ThisKeywordSample9();
obj1.x[0] = 'A';
document.writeln('obj1 Array : ' + obj1.x);
document.writeln('obj1 Array Size : ' + obj1.x.length);
var obj2 = new this.vj$.ThisKeywordSample9();
document.writeln('obj2 Array : ' + obj2.x);
document.writeln('obj2 Array Size : ' + obj2.x.length);
}
})
.endType();