package com.github.yuanqunwang.dbgen4j.seed;

import com.github.javafaker.Faker;

public class Rock {
    private final Faker faker;
    public Rock(Faker faker){
        this.faker = faker;
    }

    public String igneousRock(){
        return this.faker.fakeValuesService().resolve("rock.igneous_rock", this, this.faker);
    }

    public String sedimentaryRock(){
        return this.faker.fakeValuesService().resolve("rock.sedimentary_rock", this, this.faker);
    }
    public String metamorphicRock(){
        return this.faker.fakeValuesService().resolve("rock.metamorphic_rock", this, this.faker);
    }

    public String texture(){
        return this.faker.fakeValuesService().resolve("rock.texture", this, this.faker);
    }

    public String color(){
        return this.faker.fakeValuesService().resolve("rock.color", this, this.faker);
    }

    public String rock(){
        return this.faker.fakeValuesService().resolve("rock.rock", this, this.faker);
    }
}
