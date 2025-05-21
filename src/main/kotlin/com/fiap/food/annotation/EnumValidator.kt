package com.fiap.food.annotation

import com.fiap.food.application.domain.exceptions.InvalidEnumValueException
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.ReportAsSingleViolation
import kotlin.reflect.KClass

@Constraint(validatedBy = [EnumValidatorImpl::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@ReportAsSingleViolation
annotation class EnumValidator(
    val enumClazz: KClass<out Enum<*>>,
    val message: String = "Value is not valid",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = [],
    val required: Boolean = false
)

class EnumValidatorImpl: ConstraintValidator<EnumValidator, Any> {
    var valueList: MutableList<String>? = null
    var required: Boolean = false

    override fun isValid(target: Any?, context: ConstraintValidatorContext?): Boolean {
        return when {
            !required && target == null -> true
            required && target == null ->
                checkConstraintViolation(listOf(""), context)
            target is List<*> ->
                checkConstraintViolation(target as List<String>, context)
            else ->
                checkConstraintViolation(listOf(target as String), context)
        }
    }

    private fun checkConstraintViolation(inputValues: List<String>, context: ConstraintValidatorContext?): Boolean {
        var isValid = true
        inputValues.forEach { enumValue ->
            if (!valueList!!.contains(enumValue.uppercase())) {
                throw InvalidEnumValueException("Invalid value: '$enumValue'. Possible values: $valueList")
            }
            isValid = valueList!!.contains(enumValue.uppercase()).also {
                if (!it){
                    context!!.disableDefaultConstraintViolation()
                    context.buildConstraintViolationWithTemplate("Invalid value. Possible values: $valueList")
                        .addConstraintViolation()
                }
            }
        }
        return isValid
    }

    override fun initialize(constraintAnnotation: EnumValidator?) {
        valueList = mutableListOf()

        val enumClass = constraintAnnotation?.enumClazz

        @SuppressWarnings("rawtypes")
        val enumValArr = enumClass?.java?.enumConstants

        for (enumVal in enumValArr!!) {
            valueList?.add(enumVal.toString().uppercase())
        }
        required = constraintAnnotation.required
    }
}
