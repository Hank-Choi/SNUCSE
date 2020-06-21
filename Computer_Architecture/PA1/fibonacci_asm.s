	.option nopic
	.attribute arch, "rv64i2p0_m2p0_a2p0_f2p0_d2p0_c2p0"
	.attribute unaligned_access, 0
	.attribute stack_align, 16
	.text
	.align	1
	.globl	fibonacci
	.type	fibonacci, @function
fibonacci:
	#------Your code starts here------
	#LHS: a0, RHS: a1

	addi t0,zero,1
	sd t0,0(a0)
	addi t1,zero,2
	blt a1,t1,Exit
	sd t0,8(a0)
	addi t0,zero,2
Loop:
	bge t0,a1,Exit
	slli t1,t0,3
	add t2,a0,t1
	ld t3,-8(t2)
	ld t4,-16(t2)
	add t3,t3,t4
	sd t3,0(t2)
	addi t0,t0,1
	j Loop
Exit:
	#Load return value to reg a0
	#------Your code ends here------

	#Ret
	jr	ra
	.size	fibonacci, .-fibonacci
	.ident	"GCC: (GNU) 9.2.0"
